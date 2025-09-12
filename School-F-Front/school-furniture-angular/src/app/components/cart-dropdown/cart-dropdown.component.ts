import { Component, OnInit, OnDestroy, Input, Output, EventEmitter } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { CartService } from '../../services/cart.service';
import { CartItemResponse } from '../../models/cart.model';
import { CartSummary } from '../../models/cart-summary.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cart-dropdown',
  templateUrl: './cart-dropdown.component.html',
  styleUrls: ['./cart-dropdown.component.scss']
})
export class CartDropdownComponent implements OnInit, OnDestroy {
  @Input() isOpen = false;
  @Output() closeDropdown = new EventEmitter<void>();

  cartItems: CartItemResponse[] = [];
  cartSummary: CartSummary = {
    totalItems: 0,
    totalPrice: 0
  };
  isLoading = false;
  updatingItems: { [key: number]: boolean } = {};

  private destroy$ = new Subject<void>();

  constructor(
    private cartService: CartService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Subscribe to cart items (reactive)
    this.cartService.cartItems$
      .pipe(takeUntil(this.destroy$))
      .subscribe((items: CartItemResponse[]) => {
        this.cartItems = items;
      });

    // Subscribe to cart summary (reactive)
    this.cartService.cartSummary$
      .pipe(takeUntil(this.destroy$))
      .subscribe(summary => {
        this.cartSummary = summary;
      });

    // Initial load to populate data when component opens
    this.cartService.loadCartItems().pipe(takeUntil(this.destroy$)).subscribe();
    this.cartService.getCartSummary().pipe(takeUntil(this.destroy$)).subscribe();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  updateQuantity(item: CartItemResponse, newQuantity: number): void {
    if (newQuantity < 1) {
      this.removeItem(item);
      return;
    }

    this.updatingItems[item.product.productId] = true;
    
    this.cartService.updateCartItem(item.product.productId, newQuantity)
      .subscribe({
        next: () => {
          this.updatingItems[item.product.productId] = false;
        },
        error: (error: any) => {
          console.error('Error updating cart item:', error);
          this.updatingItems[item.product.productId] = false;
        }
      });
  }

  removeItem(item: CartItemResponse): void {
    this.updatingItems[item.product.productId] = true;
    
    this.cartService.removeFromCart(item.product.productId)
      .subscribe({
        next: () => {
          this.updatingItems[item.product.productId] = false;
        },
        error: (error: any) => {
          console.error('Error removing cart item:', error);
          this.updatingItems[item.product.productId] = false;
        }
      });
  }

  clearCart(): void {
    if (confirm('Are you sure you want to clear your cart?')) {
      this.isLoading = true;
      
      this.cartService.clearCart()
        .subscribe({
          next: () => {
            this.isLoading = false;
          },
          error: (error) => {
            console.error('Error clearing cart:', error);
            this.isLoading = false;
          }
        });
    }
  }

  goToCart(): void {
    this.closeDropdown.emit();
    this.router.navigate(['/cart']);
  }

  goToCheckout(): void {
    this.closeDropdown.emit();
    // Navigate to checkout when implemented
    console.log('Checkout functionality to be implemented');
  }

  onBackdropClick(): void {
    this.closeDropdown.emit();
  }

  onDropdownClick(event: Event): void {
    event.stopPropagation();
  }

  isItemUpdating(productId: number): boolean {
    return !!this.updatingItems[productId];
  }

  getItemTotal(item: CartItemResponse): number {
    return item.product.price * item.quantity;
  }

  trackByProductId(index: number, item: CartItemResponse): number {
    return item.product.productId;
  }
}
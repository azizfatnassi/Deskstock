import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { CartService } from '../../services/cart.service';
import { CartItemResponse, CartSummary } from '../../models/cart.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit, OnDestroy {
  cartItems: CartItemResponse[] = [];
  cartSummary: CartSummary = { totalItems: 0, totalPrice: 0 };
  isLoading = false;
  updatingItems: { [key: number]: boolean } = {};
  
  private destroy$ = new Subject<void>();

  constructor(
    private cartService: CartService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadCartItems();
    
    // Subscribe to cart updates
    this.cartService.cartItems$
      .pipe(takeUntil(this.destroy$))
      .subscribe(items => {
        this.cartItems = items;
      });

    this.cartService.cartSummary$
      .pipe(takeUntil(this.destroy$))
      .subscribe(summary => {
        this.cartSummary = summary;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadCartItems(): void {
    this.isLoading = true;
    this.cartService.loadCartItems()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (items: CartItemResponse[]) => {
          this.cartItems = items;
          this.isLoading = false;
        },
        error: (error: any) => {
          console.error('Error loading cart items:', error);
          this.isLoading = false;
        }
      });
  }

  updateQuantity(item: CartItemResponse, newQuantity: number): void {
    if (newQuantity < 1) {
      this.removeItem(item);
      return;
    }

    this.updatingItems[item.product.id] = true;
    
    this.cartService.updateCartItem(item.product.id, newQuantity)
      .subscribe({
        next: () => {
          this.updatingItems[item.product.id] = false;
        },
        error: (error: any) => {
          console.error('Error updating cart item:', error);
          this.updatingItems[item.product.id] = false;
        }
      });
  }

  removeItem(item: CartItemResponse): void {
    this.updatingItems[item.product.id] = true;
    
    this.cartService.removeFromCart(item.product.id)
      .subscribe({
        next: () => {
          this.updatingItems[item.product.id] = false;
        },
        error: (error: any) => {
          console.error('Error removing cart item:', error);
          this.updatingItems[item.product.id] = false;
        }
      });
  }

  clearCart(): void {
    if (confirm('Are you sure you want to clear your cart?')) {
      this.cartService.clearCart()
        .subscribe({
          next: () => {
            console.log('Cart cleared successfully');
          },
          error: (error) => {
            console.error('Error clearing cart:', error);
          }
        });
    }
  }

  continueShopping(): void {
    this.router.navigate(['/products']);
  }

  proceedToCheckout(): void {
    // Navigate to checkout when implemented
    console.log('Checkout functionality to be implemented');
  }

  getItemTotal(item: CartItemResponse): number {
    return item.product.price * item.quantity;
  }

  isItemUpdating(productId: number): boolean {
    return this.updatingItems[productId] || false;
  }

  trackByProductId(index: number, item: CartItemResponse): number {
    return item.product.id;
  }
}

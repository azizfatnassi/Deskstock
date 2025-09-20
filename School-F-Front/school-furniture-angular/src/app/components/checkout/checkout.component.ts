import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { CartService, OrderService, AuthService } from '../../services';
import { CartItem, CartOrderRequest, Order } from '../../models';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss']
})
export class CheckoutComponent implements OnInit, OnDestroy {
  checkoutForm: FormGroup;
  cartItems: CartItem[] = [];
  cartTotal = 0;
  isLoading = false;
  isSubmitting = false;
  errorMessage = '';
  successMessage = '';
  currentUser: any = null;
  
  private destroy$ = new Subject<void>();

  constructor(
    private fb: FormBuilder,
    private cartService: CartService,
    private orderService: OrderService,
    private authService: AuthService,
    public router: Router
  ) {
    this.checkoutForm = this.createForm();
  }

  ngOnInit(): void {
    this.loadCurrentUser();
    this.loadCartData();
  }
  
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
  
  private createForm(): FormGroup {
    return this.fb.group({
      customerName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      customerEmail: ['', [Validators.required, Validators.email, Validators.maxLength(100)]],
      customerPhone: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(20)]],
      shippingAddress: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(500)]],
      notes: ['', [Validators.maxLength(1000)]]
    });
  }
  
  private loadCurrentUser(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser) {
      this.currentUser = currentUser;
      this.checkoutForm.patchValue({
         customerName: currentUser.firstName && currentUser.lastName ? `${currentUser.firstName} ${currentUser.lastName}` : currentUser.username,
         customerEmail: currentUser.email || ''
       });
    }
  }
  
  private loadCartData(): void {
    this.isLoading = true;
    
    this.cartService.cartItems$
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (items: any[]) => {
          this.cartItems = items;
          this.cartService.getCartSummary().subscribe(summary => {
            this.cartTotal = summary.totalPrice;
          });
          this.isLoading = false;
          
          if (this.cartItems.length === 0) {
            this.errorMessage = 'Your cart is empty. Please add items before checkout.';
          }
        },
        error: (error: any) => {
          console.error('Error loading cart:', error);
          this.errorMessage = 'Failed to load cart data. Please try again.';
          this.isLoading = false;
        }
      });
  }
  
  onSubmit(): void {
    if (this.checkoutForm.invalid) {
      this.markFormGroupTouched();
      return;
    }
    
    if (this.cartItems.length === 0) {
      this.errorMessage = 'Your cart is empty. Please add items before checkout.';
      return;
    }
    
    this.isSubmitting = true;
    this.errorMessage = '';
    this.successMessage = '';
    
    const orderData: CartOrderRequest = {
      customerName: this.checkoutForm.value.customerName,
      customerEmail: this.checkoutForm.value.customerEmail,
      customerPhone: this.checkoutForm.value.customerPhone,
      shippingAddress: this.checkoutForm.value.shippingAddress,
      notes: this.checkoutForm.value.notes || undefined
    };
    
    this.orderService.createOrderFromCart(orderData)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success && response.order) {
            this.successMessage = 'Order placed successfully! Redirecting to products page...';
            
            // Clear cart and redirect to products page after a short delay
            this.cartService.clearCart().subscribe();
            setTimeout(() => {
              this.router.navigate(['/products']);
            }, 2000);
          } else {
            this.errorMessage = response.message || 'Failed to place order. Please try again.';
          }
          this.isSubmitting = false;
        },
        error: (error) => {
          console.error('Error placing order:', error);
          this.errorMessage = error.error?.message || 'Failed to place order. Please try again.';
          this.isSubmitting = false;
        }
      });
  }
  
  private markFormGroupTouched(): void {
    Object.keys(this.checkoutForm.controls).forEach(key => {
      const control = this.checkoutForm.get(key);
      if (control) {
        control.markAsTouched();
      }
    });
  }
  
  isFieldInvalid(fieldName: string): boolean {
    const field = this.checkoutForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }
  
  getFieldError(fieldName: string): string {
    const field = this.checkoutForm.get(fieldName);
    if (field && field.errors && (field.dirty || field.touched)) {
      if (field.errors['required']) {
        return `${this.getFieldLabel(fieldName)} is required.`;
      }
      if (field.errors['email']) {
        return 'Please enter a valid email address.';
      }
      if (field.errors['minlength']) {
        return `${this.getFieldLabel(fieldName)} must be at least ${field.errors['minlength'].requiredLength} characters.`;
      }
      if (field.errors['maxlength']) {
        return `${this.getFieldLabel(fieldName)} cannot exceed ${field.errors['maxlength'].requiredLength} characters.`;
      }
    }
    return '';
  }
  
  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      customerName: 'Customer name',
      customerEmail: 'Email address',
      customerPhone: 'Phone number',
      shippingAddress: 'Shipping address',
      notes: 'Notes'
    };
    return labels[fieldName] || fieldName;
  }
  
  goBackToCart(): void {
    this.router.navigate(['/cart']);
  }
  
  getTotalItems(): number {
    return this.cartItems.reduce((total, item) => total + item.quantity, 0);
  }
}

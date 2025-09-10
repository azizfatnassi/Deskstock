import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { AuthService, CartService } from '../../services';
import { UserResponse, CartSummary } from '../../models';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {
  currentUser: any = null;
  isAuthenticated = false;
  cartSummary: CartSummary = { totalItems: 0, totalPrice: 0 };
  previousCartItems = 0;
  searchQuery = '';
  isCartDropdownOpen = false;
  cartBadgeAnimating = false;
  
  private destroy$ = new Subject<void>();

  constructor(
    private authService: AuthService,
    private cartService: CartService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Subscribe to authentication state
    this.authService.currentUser$
      .pipe(takeUntil(this.destroy$))
      .subscribe(user => {
        this.currentUser = user;
        this.isAuthenticated = !!user;
      });

    // Subscribe to cart summary with animation handling
    this.cartService.cartSummary$
      .pipe(takeUntil(this.destroy$))
      .subscribe(summary => {
        // Trigger animation if cart count changed
        if (summary.totalItems !== this.previousCartItems && summary.totalItems > 0) {
          this.triggerCartBadgeAnimation();
        }
        this.previousCartItems = this.cartSummary.totalItems;
        this.cartSummary = summary;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onSearch(): void {
    if (this.searchQuery.trim()) {
      this.router.navigate(['/products'], { 
        queryParams: { search: this.searchQuery.trim() } 
      });
    }
  }

  onLogout(): void {
    this.authService.logout();
    this.cartService.resetCartState();
    this.router.navigate(['/']);
  }

  toggleCartDropdown(): void {
    this.isCartDropdownOpen = !this.isCartDropdownOpen;
  }

  closeCartDropdown(): void {
    this.isCartDropdownOpen = false;
  }

  navigateToCart(): void {
    this.router.navigate(['/cart']);
  }

  navigateToProfile(): void {
    this.router.navigate(['/profile']);
  }

  navigateToLogin(): void {
    this.router.navigate(['/login']);
  }

  navigateToRegister(): void {
    this.router.navigate(['/register']);
  }

  triggerCartBadgeAnimation(): void {
    this.cartBadgeAnimating = true;
    // Reset animation after it completes
    setTimeout(() => {
      this.cartBadgeAnimating = false;
    }, 600); // Match the bounce animation duration
  }

  // Helper method to check if cart badge should animate
  shouldAnimateCartBadge(): boolean {
    return this.cartBadgeAnimating && this.cartSummary.totalItems > 0;
  }
}

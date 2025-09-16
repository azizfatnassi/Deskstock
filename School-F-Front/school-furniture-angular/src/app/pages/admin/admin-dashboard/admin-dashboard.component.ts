import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { UserService } from '../../../services/user.service';
import { ProductService } from '../../../services/product.service';
import { UserResponse, ProductResponse } from '../../../models';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  currentUser: UserResponse | null = null;
  totalUsers = 0;
  totalProducts = 0;
  isLoading = true;
  error = '';

  constructor(
    private router: Router,
    private authService: AuthService,
    private userService: UserService,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    this.loadCurrentUser();
    this.loadDashboardStats();
  }

  private loadCurrentUser(): void {
    this.currentUser = this.authService.getCurrentUser();
  }

  private loadDashboardStats(): void {
    this.isLoading = true;
    this.error = '';

    // Load user count
    this.userService.getAllUsers().subscribe({
      next: (users) => {
        this.totalUsers = users.length;
        this.checkLoadingComplete();
      },
      error: (error) => {
        console.error('Error loading users:', error);
        this.error = 'Failed to load user statistics';
        this.checkLoadingComplete();
      }
    });

    // Load product count
    this.productService.getAllProducts(0, 1000).subscribe({
      next: (response) => {
        this.totalProducts = response.totalElements;
        this.checkLoadingComplete();
      },
      error: (error) => {
        console.error('Error loading products:', error);
        this.error = 'Failed to load product statistics';
        this.checkLoadingComplete();
      }
    });
  }

  private checkLoadingComplete(): void {
    // Simple check - if we have both counts or an error, stop loading
    if (this.totalUsers >= 0 && this.totalProducts >= 0) {
      this.isLoading = false;
    }
  }

  // Navigation methods
  navigateToUsers(): void {
    this.router.navigate(['/admin/users']);
  }

  navigateToProducts(): void {
    this.router.navigate(['/admin/products']);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  goToMainSite(): void {
    this.router.navigate(['/home']);
  }
}
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { ProductService } from '../../../services/product.service';

interface DashboardStats {
  totalProducts: number;
  lowStockProducts: number;
  outOfStockProducts: number;
  totalUsers: number;
}

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {
  currentUser: any;
  stats: DashboardStats = {
    totalProducts: 0,
    lowStockProducts: 0,
    outOfStockProducts: 0,
    totalUsers: 0
  };
  
  activeSection: string = 'overview';
  isLoading: boolean = true;
  error: string = '';
  
  constructor(
    private authService: AuthService,
    private productService: ProductService,
    private router: Router
  ) {}
  
  ngOnInit(): void {
    this.loadUserData();
    this.loadDashboardStats();
  }
  
  loadUserData(): void {
    this.currentUser = this.authService.getCurrentUser();
    if (!this.currentUser || !this.authService.isAdmin()) {
      this.router.navigate(['/admin/login']);
    }
  }
  
  loadDashboardStats(): void {
    this.isLoading = true;
    this.error = '';
    
    // Load product statistics
    this.productService.getAllProducts().subscribe({
      next: (products) => {
        this.stats.totalProducts = products.length;
        this.stats.lowStockProducts = products.filter(p => p.stock > 0 && p.stock <= 10).length;
        this.stats.outOfStockProducts = products.filter(p => p.stock === 0).length;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading dashboard stats:', error);
        this.error = 'Failed to load dashboard statistics';
        this.isLoading = false;
      }
    });
  }
  
  setActiveSection(section: string): void {
    this.activeSection = section;
  }
  
  navigateToProducts(): void {
    this.router.navigate(['/admin/products']);
  }
  
  navigateToStatistics(): void {
    this.router.navigate(['/admin/statistics']);
  }
  
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/admin/login']);
  }
  
  refreshStats(): void {
    this.loadDashboardStats();
  }
}

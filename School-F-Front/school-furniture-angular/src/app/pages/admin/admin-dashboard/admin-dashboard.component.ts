import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { ProductService } from '../../../services/product.service';
import { UserService } from '../../../services/user.service';

interface DashboardStats {
  totalProducts: number;
  lowStockItems: number;
  outOfStockItems: number;
  totalUsers: number;
  totalRevenue?: number;
  totalOrders?: number;
  totalCustomers?: number;
  revenueChange?: number;
  ordersChange?: number;
  productsChange?: number;
  customersChange?: number;
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
    lowStockItems: 0,
    outOfStockItems: 0,
    totalUsers: 0,
    totalRevenue: 0,
    totalOrders: 0,
    totalCustomers: 0,
    revenueChange: 0,
    ordersChange: 0,
    productsChange: 0,
    customersChange: 0
  };
  
  activeSection: string = 'overview';
  isLoading: boolean = true;
  error: string = '';
  sidebarOpen: boolean = false;
  
  constructor(
    private authService: AuthService,
    private productService: ProductService,
    private userService: UserService,
    private router: Router
  ) {}
  
  ngOnInit(): void {
    this.loadUserData();
    this.loadDashboardStats();
    this.refreshUsers();
    this.refreshProducts();
  }
  
  loadUserData(): void {
    this.currentUser = this.authService.getCurrentUser();
    
    if (!this.currentUser || !this.authService.isAdmin()) {
      this.router.navigate(['/admin/login']);
      return;
    }
  }
   
   loadDashboardStats(): void {
    this.isLoading = true;
    this.error = '';
    
    // Load product statistics
    this.productService.getAllProducts().subscribe({
      next: (response) => {
        const products = response.content || [];
        const productStats = {
          totalProducts: products.length,
          lowStockItems: products.filter((p: any) => p.stockQuantity > 0 && p.stockQuantity <= 10).length,
          outOfStockItems: products.filter((p: any) => p.stockQuantity === 0).length
        };
        
        // Load user statistics
        this.userService.getAllUsers().subscribe({
          next: (users) => {
            this.stats = {
              ...productStats,
              totalUsers: users.length
            };
            this.isLoading = false;
          },
          error: (error) => {
            console.error('Error loading user stats:', error);
            // Still show product stats even if user stats fail
            this.stats = {
              ...productStats,
              totalUsers: 0
            };
            this.isLoading = false;
          }
        });
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
    
    // Close sidebar when section is selected
    this.closeSidebar();
    
    // Load user data when switching to users section if not already loaded
    if (section === 'users' && this.users.length === 0) {
      this.refreshUsers();
    }
    
    // Load product data when switching to products section if not already loaded
    if (section === 'products' && this.products.length === 0) {
      this.refreshProducts();
    }
  }
  
  getSectionTitle(): string {
    switch (this.activeSection) {
      case 'overview':
        return 'Dashboard Overview';
      case 'users':
        return 'User Management';
      case 'products':
        return 'Product Management';
      case 'statistics':
        return 'Statistics & Analytics';
      default:
        return 'Admin Dashboard';
    }
  }
  
  // User management properties
  isLoadingUsers: boolean = false;
  users: any[] = [];
  filteredUsers: any[] = [];
  searchTerm: string = '';
  selectedRole: string = '';
  currentPage: number = 1;
  pageSize: number = 10;
  successMessage: string = '';
  
  // User management methods
  refreshUsers(): void {
    this.isLoadingUsers = true;
    this.userService.getAllUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.applyFilters();
        this.isLoadingUsers = false;
      },
      error: (error) => {
        console.error('Error loading users:', error);
        this.error = 'Failed to load users';
        this.isLoadingUsers = false;
      }
    });
  }
  
  onSearchChange(): void {
    this.applyFilters();
  }
  
  onRoleFilterChange(): void {
    this.applyFilters();
  }
  
  applyFilters(): void {
    this.filteredUsers = this.users.filter(user => {
      const matchesSearch = !this.searchTerm || 
        user.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        user.email.toLowerCase().includes(this.searchTerm.toLowerCase());
      const matchesRole = !this.selectedRole || user.role === this.selectedRole;
      return matchesSearch && matchesRole;
    });
    this.currentPage = 1;
  }
  
  getPaginatedUsers(): any[] {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    return this.filteredUsers.slice(startIndex, endIndex);
  }
  
  getTotalPages(): number {
    return Math.ceil(this.filteredUsers.length / this.pageSize);
  }
  
  goToPage(page: number): void {
    if (page >= 1 && page <= this.getTotalPages()) {
      this.currentPage = page;
    }
  }
  
  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleDateString();
  }
  
  editUser(user: any): void {
    console.log('Edit user:', user);
    // Edit user logic will be implemented
  }
  
  deleteUser(user: any): void {
    if (confirm(`Are you sure you want to delete user ${user.name}?`)) {
      console.log('Delete user:', user);
      // Delete user logic will be implemented
    }
  }
  
  // Product management properties
  isLoadingProducts: boolean = false;
  products: any[] = [];
  filteredProducts: any[] = [];
  productSearchTerm: string = '';
  selectedCategory: string = '';
  stockFilter: string = 'all';
  productCurrentPage: number = 1;
  productPageSize: number = 12;
  
  // Product management methods
  refreshProducts(): void {
    this.isLoadingProducts = true;
    this.productService.getAllProducts().subscribe({
      next: (response) => {
        this.products = response.content || [];
        this.applyProductFilters();
        this.isLoadingProducts = false;
      },
      error: (error) => {
        console.error('Error loading products:', error);
        this.error = 'Failed to load products';
        this.isLoadingProducts = false;
      }
    });
  }
  
  onProductSearchChange(): void {
    this.applyProductFilters();
  }
  
  onCategoryChange(): void {
    this.applyProductFilters();
  }
  
  onStockFilterChange(): void {
    this.applyProductFilters();
  }
  
  applyProductFilters(): void {
    this.filteredProducts = this.products.filter(product => {
      const matchesSearch = !this.productSearchTerm || 
        product.name.toLowerCase().includes(this.productSearchTerm.toLowerCase()) ||
        product.description.toLowerCase().includes(this.productSearchTerm.toLowerCase());
      const matchesCategory = !this.selectedCategory || product.category === this.selectedCategory;
      
      let matchesStock = true;
      if (this.stockFilter === 'in-stock') {
        matchesStock = product.stockQuantity > 10;
      } else if (this.stockFilter === 'low-stock') {
        matchesStock = product.stockQuantity > 0 && product.stockQuantity <= 10;
      } else if (this.stockFilter === 'out-of-stock') {
        matchesStock = product.stockQuantity === 0;
      }
      
      return matchesSearch && matchesCategory && matchesStock;
    });
    this.productCurrentPage = 1;
  }
  
  getPaginatedProducts(): any[] {
    const startIndex = (this.productCurrentPage - 1) * this.productPageSize;
    const endIndex = startIndex + this.productPageSize;
    return this.filteredProducts.slice(startIndex, endIndex);
  }
  
  getProductTotalPages(): number {
    return Math.ceil(this.filteredProducts.length / this.productPageSize);
  }
  
  goToProductPage(page: number): void {
    if (page >= 1 && page <= this.getProductTotalPages()) {
      this.productCurrentPage = page;
    }
  }
  
  getStockStatus(stockQuantity: number): string {
    if (stockQuantity === 0) return 'out-of-stock';
    if (stockQuantity <= 10) return 'low-stock';
    return 'in-stock';
  }
  
  showAddProductForm(): void {
    console.log('Show add product form');
    // Add product form logic will be implemented
  }
  
  editProduct(product: any): void {
    console.log('Edit product:', product);
    // Edit product logic will be implemented
  }
  
  deleteProduct(product: any): void {
    if (confirm(`Are you sure you want to delete product ${product.name}?`)) {
      console.log('Delete product:', product);
      // Delete product logic will be implemented
    }
  }
  
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/admin/login']);
  }
  
  refreshStats(): void {
    this.loadDashboardStats();
  }
  
  // Statistics properties
  selectedPeriod: string = '30days';
  chartLabels: string[] = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'];
  
  // Chart data properties
  revenueChartData: any[] = [
    { label: 'Jan', value: 12000 },
    { label: 'Feb', value: 15000 },
    { label: 'Mar', value: 18000 },
    { label: 'Apr', value: 14000 },
    { label: 'May', value: 20000 },
    { label: 'Jun', value: 22000 }
  ];
  
  ordersChartData: any[] = [
    { label: 'Jan', value: 120 },
    { label: 'Feb', value: 150 },
    { label: 'Mar', value: 180 },
    { label: 'Apr', value: 140 },
    { label: 'May', value: 200 },
    { label: 'Jun', value: 220 }
  ];
  
  // Category distribution data
  categoryDistribution: any[] = [
    { name: 'Chairs', count: 45, percentage: 35, color: '#3b82f6' },
    { name: 'Tables', count: 32, percentage: 25, color: '#10b981' },
    { name: 'Desks', count: 28, percentage: 22, color: '#f59e0b' },
    { name: 'Storage', count: 23, percentage: 18, color: '#ef4444' }
  ];
  
  // Top selling products data
  topSellingProducts: any[] = [
    { name: 'Executive Office Chair', sales: 156, revenue: 23400, image: 'assets/images/chair1.jpg' },
    { name: 'Modern Conference Table', sales: 89, revenue: 44500, image: 'assets/images/table1.jpg' },
    { name: 'Standing Desk Pro', sales: 67, revenue: 33500, image: 'assets/images/desk1.jpg' },
    { name: 'Filing Cabinet Deluxe', sales: 45, revenue: 13500, image: 'assets/images/storage1.jpg' }
  ];
  
  // Recent activity data
  recentActivities: any[] = [
    { icon: 'shopping-cart', message: 'New order #1234 received', time: '2 minutes ago', type: 'order' },
    { icon: 'user-plus', message: 'New customer John Doe registered', time: '15 minutes ago', type: 'user' },
    { icon: 'package', message: 'Product "Office Chair" updated', time: '1 hour ago', type: 'product' },
    { icon: 'credit-card', message: 'Payment of $1,250 received', time: '2 hours ago', type: 'payment' },
    { icon: 'truck', message: 'Order #1230 shipped', time: '3 hours ago', type: 'shipping' }
  ];
  
  // Stock alerts data
  lowStockAlerts: any[] = [
    { name: 'Executive Office Chair', stock: 5, threshold: 10 },
    { name: 'Conference Table', stock: 3, threshold: 10 },
    { name: 'Filing Cabinet', stock: 7, threshold: 10 }
  ];
  
  outOfStockAlerts: any[] = [
    { name: 'Standing Desk Pro', stock: 0 },
    { name: 'Ergonomic Mouse Pad', stock: 0 }
  ];
  
  // Statistics methods
  onPeriodChange(): void {
    console.log('Period changed to:', this.selectedPeriod);
    // Chart update logic will be implemented when chart library is integrated
    this.updateChartData();
  }
  
  updateChartData(): void {
    // Update chart data based on selected period
    // This would typically fetch new data from the backend
    console.log('Updating chart data for period:', this.selectedPeriod);
  }
  
  viewAllProducts(): void {
    this.setActiveSection('products');
  }
  
  viewProduct(productName: string): void {
    console.log('Viewing product:', productName);
    // Navigate to specific product or show product details
  }
  
  // Sidebar toggle methods
  toggleSidebar(): void {
    this.sidebarOpen = !this.sidebarOpen;
  }
  
  closeSidebar(): void {
    this.sidebarOpen = false;
  }
  
  getActivityColor(type: string): string {
    const colors: { [key: string]: string } = {
      'order': 'order-color',
      'user': 'user-color',
      'product': 'product-color',
      'payment': 'payment-color',
      'shipping': 'shipping-color'
    };
    return colors[type] || 'default-color';
  }
}

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { ProductService } from '../../../services/product.service';

interface StatisticsData {
  totalProducts: number;
  totalRevenue: number;
  totalOrders: number;
  totalCustomers: number;
  lowStockProducts: number;
  outOfStockProducts: number;
  monthlyRevenue: number[];
  monthlyOrders: number[];
  categoryDistribution: { name: string; value: number; percentage: number }[];
  topSellingProducts: { name: string; sales: number; revenue: number }[];
  recentActivity: { type: string; message: string; timestamp: Date }[];
}

@Component({
  selector: 'app-admin-statistics',
  templateUrl: './admin-statistics.component.html',
  styleUrls: ['./admin-statistics.component.scss']
})
export class AdminStatisticsComponent implements OnInit {
  isLoading = true;
  error: string | null = null;
  statistics: StatisticsData | null = null;
  selectedPeriod = 'month';
  
  // Chart data
  revenueChartData: number[] = [];
  ordersChartData: number[] = [];
  chartLabels: string[] = [];
  
  // Date ranges
  periods = [
    { value: 'week', label: 'Last 7 Days' },
    { value: 'month', label: 'Last 30 Days' },
    { value: 'quarter', label: 'Last 3 Months' },
    { value: 'year', label: 'Last 12 Months' }
  ];

  constructor(
    private router: Router,
    private authService: AuthService,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    this.checkAdminAccess();
    this.loadStatistics();
  }

  private checkAdminAccess(): void {
    const user = this.authService.getCurrentUser();
    if (!user || user.role !== 'ADMIN') {
      this.router.navigate(['/admin/login']);
      return;
    }
  }

  loadStatistics(): void {
    this.isLoading = true;
    this.error = null;

    // Simulate API call - replace with actual service call
    setTimeout(() => {
      try {
        this.statistics = this.generateMockStatistics();
        this.updateChartData();
        this.isLoading = false;
      } catch (error) {
        this.error = 'Failed to load statistics. Please try again.';
        this.isLoading = false;
      }
    }, 1000);
  }

  private generateMockStatistics(): StatisticsData {
    const currentDate = new Date();
    const monthlyRevenue = Array.from({ length: 12 }, () => Math.floor(Math.random() * 50000) + 10000);
    const monthlyOrders = Array.from({ length: 12 }, () => Math.floor(Math.random() * 200) + 50);
    
    return {
      totalProducts: 156,
      totalRevenue: 284750,
      totalOrders: 1247,
      totalCustomers: 892,
      lowStockProducts: 12,
      outOfStockProducts: 3,
      monthlyRevenue,
      monthlyOrders,
      categoryDistribution: [
        { name: 'Chairs', value: 45, percentage: 28.8 },
        { name: 'Desks', value: 38, percentage: 24.4 },
        { name: 'Tables', value: 32, percentage: 20.5 },
        { name: 'Storage', value: 25, percentage: 16.0 },
        { name: 'Accessories', value: 16, percentage: 10.3 }
      ],
      topSellingProducts: [
        { name: 'Executive Office Chair', sales: 89, revenue: 26700 },
        { name: 'Standing Desk Pro', sales: 67, revenue: 33500 },
        { name: 'Conference Table', sales: 45, revenue: 22500 },
        { name: 'Ergonomic Chair', sales: 78, revenue: 15600 },
        { name: 'Storage Cabinet', sales: 56, revenue: 11200 }
      ],
      recentActivity: [
        { type: 'order', message: 'New order #1247 received', timestamp: new Date(Date.now() - 300000) },
        { type: 'product', message: 'Product "Office Chair" updated', timestamp: new Date(Date.now() - 600000) },
        { type: 'stock', message: 'Low stock alert for "Desk Lamp"', timestamp: new Date(Date.now() - 900000) },
        { type: 'customer', message: 'New customer registration', timestamp: new Date(Date.now() - 1200000) },
        { type: 'order', message: 'Order #1246 completed', timestamp: new Date(Date.now() - 1500000) }
      ]
    };
  }

  private updateChartData(): void {
    if (!this.statistics) return;

    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    const currentMonth = new Date().getMonth();
    
    // Get last 6 months for display
    this.chartLabels = [];
    this.revenueChartData = [];
    this.ordersChartData = [];
    
    for (let i = 5; i >= 0; i--) {
      const monthIndex = (currentMonth - i + 12) % 12;
      this.chartLabels.push(months[monthIndex]);
      this.revenueChartData.push(this.statistics.monthlyRevenue[monthIndex]);
      this.ordersChartData.push(this.statistics.monthlyOrders[monthIndex]);
    }
  }

  onPeriodChange(): void {
    this.loadStatistics();
  }

  navigateToDashboard(): void {
    this.router.navigate(['/admin/dashboard']);
  }

  navigateToProducts(): void {
    this.router.navigate(['/admin/products']);
  }

  refreshStatistics(): void {
    this.loadStatistics();
  }

  getActivityIcon(type: string): string {
    switch (type) {
      case 'order': return '📦';
      case 'product': return '📝';
      case 'stock': return '⚠️';
      case 'customer': return '👤';
      default: return '📊';
    }
  }

  getActivityColor(type: string): string {
    switch (type) {
      case 'order': return 'text-blue-600';
      case 'product': return 'text-green-600';
      case 'stock': return 'text-yellow-600';
      case 'customer': return 'text-purple-600';
      default: return 'text-gray-600';
    }
  }

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount);
  }

  formatNumber(num: number): string {
    return new Intl.NumberFormat('en-US').format(num);
  }

  getTimeAgo(date: Date): string {
    const now = new Date();
    const diffInMinutes = Math.floor((now.getTime() - date.getTime()) / (1000 * 60));
    
    if (diffInMinutes < 1) return 'Just now';
    if (diffInMinutes < 60) return `${diffInMinutes}m ago`;
    
    const diffInHours = Math.floor(diffInMinutes / 60);
    if (diffInHours < 24) return `${diffInHours}h ago`;
    
    const diffInDays = Math.floor(diffInHours / 24);
    return `${diffInDays}d ago`;
  }
}

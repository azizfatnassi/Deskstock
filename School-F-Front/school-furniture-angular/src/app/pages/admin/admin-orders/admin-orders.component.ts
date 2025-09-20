import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { OrderService } from '../../../services/order.service';
import { Order, OrderStatistics } from '../../../models';

@Component({
  selector: 'app-admin-orders',
  templateUrl: './admin-orders.component.html',
  styleUrls: ['./admin-orders.component.scss']
})
export class AdminOrdersComponent implements OnInit, OnDestroy {
  orders: Order[] = [];
  filteredOrders: Order[] = [];
  statistics: OrderStatistics | null = null;
  isLoading = true;
  errorMessage = '';
  successMessage = '';
  
  // Filters
  selectedStatus = 'all';
  searchQuery = '';
  sortBy = 'date';
  sortOrder = 'desc';
  
  private destroy$ = new Subject<void>();

  constructor(
    private orderService: OrderService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadOrders();
    this.loadStatistics();
  }
  
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
  
  private loadOrders(): void {
    this.isLoading = true;
    this.errorMessage = '';
    
    this.orderService.getAllOrders()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response: Order[]) => {
          this.orders = response;
          this.filteredOrders = [...this.orders];
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error loading orders:', error);
          this.errorMessage = error.error?.message || 'Failed to load orders.';
          this.isLoading = false;
        }
      });
  }
  
  private loadStatistics(): void {
    this.orderService.getOrderStatistics()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response: OrderStatistics) => {
          this.statistics = response;
        },
        error: (error) => {
          console.error('Error loading statistics:', error);
        }
      });
  }
  
  applyFilters(): void {
    let filtered = [...this.orders];
    
    // Status filter
    if (this.selectedStatus !== 'all') {
      if (this.selectedStatus === 'confirmed') {
        filtered = filtered.filter(order => order.status);
      } else if (this.selectedStatus === 'pending') {
        filtered = filtered.filter(order => !order.status);
      }
    }
    
    // Search filter
    if (this.searchQuery.trim()) {
      const query = this.searchQuery.toLowerCase();
      filtered = filtered.filter(order => 
        order.orderId.toString().toLowerCase().includes(query) ||
        order.customerName.toLowerCase().includes(query) ||
        order.userName.toLowerCase().includes(query)
      );
    }
    
    // Sort
    filtered.sort((a, b) => {
      let comparison = 0;
      
      switch (this.sortBy) {
        case 'date':
          comparison = new Date(a.orderDate).getTime() - new Date(b.orderDate).getTime();
          break;
        case 'amount':
          comparison = a.totalAmount - b.totalAmount;
          break;
        case 'customer':
          comparison = a.customerName.localeCompare(b.customerName);
          break;
        case 'status':
          comparison = (a.status ? 1 : 0) - (b.status ? 1 : 0);
          break;
      }
      
      return this.sortOrder === 'desc' ? -comparison : comparison;
    });
    
    this.filteredOrders = filtered;
  }
  
  onStatusFilterChange(): void {
    this.applyFilters();
  }
  
  onSearchChange(): void {
    this.applyFilters();
  }
  
  onSortChange(): void {
    this.applyFilters();
  }
  
  confirmOrder(orderId: number): void {
    if (!confirm('Are you sure you want to confirm this order?')) {
      return;
    }
    
    this.orderService.confirmOrder(orderId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response: Order) => {
          this.successMessage = 'Order confirmed successfully!';
          this.loadOrders();
          this.loadStatistics();
          setTimeout(() => this.successMessage = '', 3000);
        },
        error: (error: any) => {
          console.error('Error confirming order:', error);
          this.errorMessage = error.error?.message || 'Failed to confirm order.';
        }
      });
  }
  
  cancelOrder(orderId: number): void {
    if (!confirm('Are you sure you want to cancel this order? This action cannot be undone.')) {
      return;
    }
    
    this.orderService.cancelOrder(orderId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.successMessage = 'Order cancelled successfully!';
          this.loadOrders();
          this.loadStatistics();
          setTimeout(() => this.successMessage = '', 3000);
        },
        error: (error: any) => {
          console.error('Error cancelling order:', error);
          this.errorMessage = error.error?.message || 'Failed to cancel order.';
        }
      });
  }
  
  viewOrderDetails(orderId: number): void {
    this.router.navigate(['/order-confirmation', orderId]);
  }
  
  getStatusText(order: Order): string {
    return order.status ? 'Confirmed' : 'Pending';
  }

  getStatusClass(order: Order): string {
    return order.status ? 'status-confirmed' : 'status-pending';
  }
  
  getTotalItems(order: Order): number {
    if (!order.orderItems) return 0;
    return order.orderItems.reduce((total, item) => total + item.quantity, 0);
  }
  
  refreshOrders(): void {
    this.loadOrders();
    this.loadStatistics();
  }
  
  goBackToDashboard(): void {
    this.router.navigate(['/admin/dashboard']);
  }
}

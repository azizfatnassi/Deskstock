import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { OrderService } from '../../services/order.service';
import { Order } from '../../models';

@Component({
  selector: 'app-my-orders',
  templateUrl: './my-orders.component.html',
  styleUrls: ['./my-orders.component.scss']
})
export class MyOrdersComponent implements OnInit, OnDestroy {
  orders: Order[] = [];
  isLoading = true;
  errorMessage = '';
  
  private destroy$ = new Subject<void>();

  constructor(
    private orderService: OrderService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadUserOrders();
  }
  
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
  
  private loadUserOrders(): void {
    this.isLoading = true;
    this.errorMessage = '';
    
    this.orderService.getMyOrders()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response: Order[]) => {
          this.orders = response.sort((a: Order, b: Order) =>
            new Date(b.orderDate).getTime() - new Date(a.orderDate).getTime()
          );
          this.isLoading = false;
        },
        error: (error: any) => {
          console.error('Error fetching orders:', error);
          this.errorMessage = error.error?.message || 'Failed to load orders.';
          this.isLoading = false;
        }
      });
  }
  
  getStatusText(order: Order): string {
    return order.status ? 'Confirmed' : 'Pending Confirmation';
  }
  
  getStatusClass(order: Order): string {
    return order.status ? 'status-confirmed' : 'status-pending';
  }
  
  getTotalItems(order: Order): number {
    if (!order.orderItems) return 0;
    return order.orderItems.reduce((total, item) => total + item.quantity, 0);
  }
  
  viewOrderDetails(orderId: number): void {
    // Navigate to order details page or show modal
    console.log('Viewing order details for order:', orderId);
    // TODO: Implement navigation to order details
    // this.router.navigate(['/orders', orderId]);
  }
  
  continueShopping(): void {
    this.router.navigate(['/products']);
  }
  
  refreshOrders(): void {
    this.loadUserOrders();
  }
}

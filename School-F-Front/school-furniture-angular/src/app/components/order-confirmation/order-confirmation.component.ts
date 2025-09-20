import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { OrderService } from '../../services';
import { Order } from '../../models';

@Component({
  selector: 'app-order-confirmation',
  templateUrl: './order-confirmation.component.html',
  styleUrls: ['./order-confirmation.component.scss']
})
export class OrderConfirmationComponent implements OnInit, OnDestroy {
  order: Order | null = null;
  isLoading = true;
  errorMessage = '';
  orderId: string | null = null;
  
  private destroy$ = new Subject<void>();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private orderService: OrderService
  ) { }

  ngOnInit(): void {
    this.route.paramMap
      .pipe(takeUntil(this.destroy$))
      .subscribe(params => {
        this.orderId = params.get('id');
        if (this.orderId) {
          this.loadOrder(this.orderId);
        } else {
          this.errorMessage = 'Order ID not found.';
          this.isLoading = false;
        }
      });
  }
  
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
  
  private loadOrder(orderId: string): void {
    this.isLoading = true;
    this.errorMessage = '';
    
    this.orderService.getOrderById(+orderId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response: Order) => {
          this.order = response;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error loading order:', error);
          this.errorMessage = error.error?.message || 'Failed to load order details.';
          this.isLoading = false;
        }
      });
  }
  
  getStatusText(): string {
    if (!this.order) return '';
    return this.order.status ? 'Confirmed' : 'Pending Confirmation';
  }
  
  getStatusClass(): string {
    if (!this.order) return '';
    return this.order.status ? 'status-confirmed' : 'status-pending';
  }
  
  getTotalItems(): number {
    if (!this.order || !this.order.orderItems) return 0;
    return this.order.orderItems.reduce((total, item) => total + item.quantity, 0);
  }
  
  continueShopping(): void {
    this.router.navigate(['/products']);
  }
  
  goToMyOrders(): void {
    this.router.navigate(['/my-orders']);
  }
  
  printOrder(): void {
    window.print();
  }
}

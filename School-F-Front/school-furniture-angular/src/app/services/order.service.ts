import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { 
  Order, 
  CreateOrderRequest, 
  CartOrderRequest,
  OrderResponse, 
  OrderStatistics, 
  OrderStatisticsResponse 
} from '../models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = `${environment.apiUrl}/orders`;
  
  // Subject to track order updates for real-time UI updates
  private ordersSubject = new BehaviorSubject<Order[]>([]);
  public orders$ = this.ordersSubject.asObservable();
  
  private pendingOrdersSubject = new BehaviorSubject<Order[]>([]);
  public pendingOrders$ = this.pendingOrdersSubject.asObservable();
  
  constructor(private http: HttpClient) {}
  
  /**
   * Create order from cart
   */
  createOrderFromCart(orderData: CartOrderRequest): Observable<OrderResponse> {
    const headers = this.getAuthHeaders();
    return this.http.post<OrderResponse>(`${this.apiUrl}/from-cart`, orderData, { headers })
      .pipe(
        tap(response => {
          if (response.success && response.order) {
            this.refreshOrders();
          }
        })
      );
  }
  
  /**
   * Create custom order
   */
  createCustomOrder(orderData: CreateOrderRequest): Observable<OrderResponse> {
    const headers = this.getAuthHeaders();
    return this.http.post<OrderResponse>(`${this.apiUrl}/custom`, orderData, { headers })
      .pipe(
        tap(response => {
          if (response.success && response.order) {
            this.refreshOrders();
          }
        })
      );
  }
  
  /**
   * Get order by ID
   */
  getOrderById(orderId: number): Observable<Order> {
    const headers = this.getAuthHeaders();
    return this.http.get<OrderResponse>(`${this.apiUrl}/${orderId}`, { headers })
      .pipe(
        map(response => {
          if (response.success && response.order) {
            return response.order;
          }
          throw new Error(response.message || 'Failed to fetch order');
        })
      );
  }
  
  /**
   * Get current user's orders
   */
  getMyOrders(): Observable<Order[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<OrderResponse>(`${this.apiUrl}/my-orders`, { headers })
      .pipe(
        map(response => {
          if (response.success && response.orders) {
            this.ordersSubject.next(response.orders);
            return response.orders;
          }
          throw new Error(response.message || 'Failed to fetch orders');
        })
      );
  }
  
  /**
   * Get all orders (admin only)
   */
  getAllOrders(): Observable<Order[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<OrderResponse>(`${this.apiUrl}/admin/all`, { headers })
      .pipe(
        map(response => {
          if (response.success && response.orders) {
            return response.orders;
          }
          throw new Error(response.message || 'Failed to fetch all orders');
        })
      );
  }
  
  /**
   * Get pending orders (admin only)
   */
  getPendingOrders(): Observable<Order[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<OrderResponse>(`${this.apiUrl}/admin/pending`, { headers })
      .pipe(
        map(response => {
          if (response.success && response.orders) {
            this.pendingOrdersSubject.next(response.orders);
            return response.orders;
          }
          throw new Error(response.message || 'Failed to fetch pending orders');
        })
      );
  }
  
  /**
   * Get confirmed orders (admin only)
   */
  getConfirmedOrders(): Observable<Order[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<OrderResponse>(`${this.apiUrl}/admin/confirmed`, { headers })
      .pipe(
        map(response => {
          if (response.success && response.orders) {
            return response.orders;
          }
          throw new Error(response.message || 'Failed to fetch confirmed orders');
        })
      );
  }
  
  /**
   * Confirm order (admin only)
   */
  confirmOrder(orderId: number): Observable<Order> {
    const headers = this.getAuthHeaders();
    return this.http.put<OrderResponse>(`${this.apiUrl}/admin/${orderId}/confirm`, {}, { headers })
      .pipe(
        map(response => {
          if (response.success && response.order) {
            this.refreshPendingOrders();
            return response.order;
          }
          throw new Error(response.message || 'Failed to confirm order');
        })
      );
  }
  
  /**
   * Cancel order
   */
  cancelOrder(orderId: number): Observable<void> {
    const headers = this.getAuthHeaders();
    return this.http.delete<OrderResponse>(`${this.apiUrl}/${orderId}`, { headers })
      .pipe(
        map(response => {
          if (response.success) {
            this.refreshOrders();
            this.refreshPendingOrders();
            return;
          }
          throw new Error(response.message || 'Failed to cancel order');
        })
      );
  }
  
  /**
   * Get order statistics (admin only)
   */
  getOrderStatistics(): Observable<OrderStatistics> {
    const headers = this.getAuthHeaders();
    return this.http.get<OrderStatisticsResponse>(`${this.apiUrl}/admin/statistics`, { headers })
      .pipe(
        map(response => {
          if (response.success && response.statistics) {
            return response.statistics;
          }
          throw new Error(response.message || 'Failed to fetch order statistics');
        })
      );
  }
  
  /**
   * Refresh orders data
   */
  refreshOrders(): void {
    this.getMyOrders().subscribe({
      next: (orders) => {
        // Orders are already updated in the subject via getMyOrders
      },
      error: (error) => {
        console.error('Error refreshing orders:', error);
      }
    });
  }
  
  /**
   * Refresh pending orders data (admin)
   */
  refreshPendingOrders(): void {
    this.getPendingOrders().subscribe({
      next: (orders) => {
        // Orders are already updated in the subject via getPendingOrders
      },
      error: (error) => {
        console.error('Error refreshing pending orders:', error);
      }
    });
  }
  
  /**
   * Clear orders data (on logout)
   */
  clearOrders(): void {
    this.ordersSubject.next([]);
    this.pendingOrdersSubject.next([]);
  }
  
  /**
   * Get current orders from subject
   */
  getCurrentOrders(): Order[] {
    return this.ordersSubject.value;
  }
  
  /**
   * Get current pending orders from subject
   */
  getCurrentPendingOrders(): Order[] {
    return this.pendingOrdersSubject.value;
  }
  
  /**
   * Check if user has any orders
   */
  hasOrders(): boolean {
    return this.getCurrentOrders().length > 0;
  }
  
  /**
   * Get total orders count
   */
  getTotalOrdersCount(): number {
    return this.getCurrentOrders().length;
  }
  
  /**
   * Get pending orders count
   */
  getPendingOrdersCount(): number {
    return this.getCurrentOrders().filter(order => !order.status).length;
  }
  
  /**
   * Get confirmed orders count
   */
  getConfirmedOrdersCount(): number {
    return this.getCurrentOrders().filter(order => order.status).length;
  }
  
  /**
   * Calculate total spent by user
   */
  getTotalSpent(): number {
    return this.getCurrentOrders()
      .filter(order => order.status) // Only confirmed orders
      .reduce((total, order) => total + order.totalAmount, 0);
  }
  
  /**
   * Get auth headers with JWT token
   */
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }
  
  /**
   * Handle HTTP errors
   */
  private handleError(error: any): Observable<never> {
    console.error('OrderService error:', error);
    throw error;
  }
}
export interface Order {
  orderId: number;
  customerName: string;
  customerEmail: string;
  customerPhone: string;
  shippingAddress: string;
  notes?: string;
  totalAmount: number;
  status: boolean; // false = pending, true = confirmed
  orderDate: string;
  userName: string;
  orderItems: OrderItem[];
}

export interface OrderItem {
  orderItemId: number;
  productId: number;
  productName: string;
  productDescription?: string;
  productImageUrl?: string;
  productCategory?: string;
  productColor?: string;
  quantity: number;
  unitPrice: number;
  subtotal: number;
}

export interface CreateOrderRequest {
  customerName: string;
  customerEmail: string;
  customerPhone: string;
  shippingAddress: string;
  notes?: string;
  orderItems?: OrderItemRequest[];
}

export interface CartOrderRequest {
  customerName: string;
  customerEmail: string;
  customerPhone: string;
  shippingAddress: string;
  notes?: string;
}

export interface OrderItemRequest {
  productId: number;
  quantity: number;
}

export interface OrderResponse {
  success: boolean;
  message: string;
  order?: Order;
  orders?: Order[];
  count?: number;
}

export interface OrderStatistics {
  totalOrders: number;
  pendingOrders: number;
  confirmedOrders: number;
  totalRevenue: number;
  averageOrderValue: number;
}

export interface OrderStatisticsResponse {
  success: boolean;
  message?: string;
  statistics?: OrderStatistics;
}

// Helper functions
export function getOrderStatusText(status: boolean): string {
  return status ? 'Confirmed' : 'Pending';
}

export function getOrderStatusClass(status: boolean): string {
  return status ? 'badge-success' : 'badge-warning';
}

export function calculateOrderTotal(orderItems: OrderItem[]): number {
  return orderItems.reduce((total, item) => total + item.subtotal, 0);
}

export function getTotalItemsCount(orderItems: OrderItem[]): number {
  return orderItems.reduce((total, item) => total + item.quantity, 0);
}

export function formatOrderDate(dateString: string): string {
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
}

export function isOrderCancellable(order: Order): boolean {
  return !order.status; // Only pending orders can be cancelled
}

export function isOrderConfirmable(order: Order): boolean {
  return !order.status; // Only pending orders can be confirmed
}
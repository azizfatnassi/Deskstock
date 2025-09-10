import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { 
  CartItem, 
  CartItemRequest, 
  CartItemResponse, 
  Cart, 
  CartSummary 
} from '../models';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private readonly baseUrl = 'http://localhost:8081/api/cart';
  
  private cartItemsSubject = new BehaviorSubject<CartItemResponse[]>([]);
  public cartItems$ = this.cartItemsSubject.asObservable();
  
  private cartSummarySubject = new BehaviorSubject<CartSummary>({ totalItems: 0, totalPrice: 0 });
  public cartSummary$ = this.cartSummarySubject.asObservable();

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {
    // Load cart items only when user is authenticated
    this.authService.isAuthenticated$.subscribe(isAuth => {
      if (isAuth) {
        this.loadCartItems().subscribe({
          error: (error) => {
            console.error('Error loading cart items:', error);
            // Reset cart state on error
            this.cartItemsSubject.next([]);
            this.updateCartSummary([]);
          }
        });
      } else {
        // Clear cart when not authenticated
        this.resetCartState();
      }
    });
  }

  // Get all cart items for current user
  loadCartItems(): Observable<CartItemResponse[]> {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser || !currentUser.id || currentUser.id === 0) {
      // Return empty array and update subjects
      this.cartItemsSubject.next([]);
      this.updateCartSummary([]);
      return new Observable(observer => {
        observer.next([]);
        observer.complete();
      });
    }
    
    const params = new HttpParams().set('userId', (currentUser.id || 0).toString());
    return this.http.get<CartItemResponse[]>(this.baseUrl, { params })
      .pipe(
        tap(items => {
          this.cartItemsSubject.next(items);
          this.updateCartSummary(items);
        })
      );
  }

  // Add item to cart
  addToCart(cartItem: CartItemRequest): Observable<CartItemResponse> {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser || !currentUser.id || currentUser.id === 0) {
      throw new Error('User not authenticated');
    }
    
    if (!cartItem || !cartItem.productId || !cartItem.quantity) {
      throw new Error('Invalid cart item data');
    }
    
    const params = new HttpParams()
      .set('userId', (currentUser.id || 0).toString())
      .set('productId', (cartItem.productId || 0).toString())
      .set('quantity', (cartItem.quantity || 1).toString());
    
    return this.http.post<CartItemResponse>(`${this.baseUrl}/add`, null, { params })
      .pipe(
        tap(() => this.refreshCartItems())
      );
  }

  // Update cart item quantity
  updateCartItem(itemId: number, quantity: number): Observable<CartItemResponse> {
    return this.http.put<CartItemResponse>(`${this.baseUrl}/items/${itemId}`, { quantity })
      .pipe(
        tap(() => this.refreshCartItems())
      );
  }

  // Remove item from cart
  removeFromCart(itemId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/items/${itemId}`)
      .pipe(
        tap(() => this.refreshCartItems())
      );
  }

  // Clear entire cart
  clearCart(): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/clear`)
      .pipe(
        tap(() => {
          this.cartItemsSubject.next([]);
          this.cartSummarySubject.next({ totalItems: 0, totalPrice: 0 });
        })
      );
  }

  // Get cart summary (total items and price)
  getCartSummary(): Observable<CartSummary> {
    return this.http.get<CartSummary>(`${this.baseUrl}/summary`)
      .pipe(
        tap(summary => this.cartSummarySubject.next(summary))
      );
  }

  // Get current cart items count
  getCartItemsCount(): number {
    return this.cartSummarySubject.value.totalItems;
  }

  // Get current cart total price
  getCartTotalPrice(): number {
    return this.cartSummarySubject.value.totalPrice;
  }

  // Check if product is in cart
  isProductInCart(productId: number): boolean {
    const items = this.cartItemsSubject.value;
    return items.some(item => item.product.id === productId);
  }

  // Get cart item by product ID
  getCartItemByProductId(productId: number): CartItemResponse | undefined {
    const items = this.cartItemsSubject.value;
    return items.find(item => item.product.id === productId);
  }

  // Add or update cart item (convenience method)
  addOrUpdateCartItem(productId: number, quantity: number): Observable<CartItemResponse> {
    const existingItem = this.getCartItemByProductId(productId);
    
    if (existingItem) {
      // Update existing item
      const newQuantity = existingItem.quantity + quantity;
      return this.updateCartItem(existingItem.id, newQuantity);
    } else {
      // Add new item
      return this.addToCart({ productId, quantity });
    }
  }

  // Private helper methods
  private refreshCartItems(): void {
    this.loadCartItems().subscribe();
  }

  private updateCartSummary(items: CartItemResponse[]): void {
    const totalItems = items.reduce((sum, item) => sum + item.quantity, 0);
    const totalPrice = items.reduce((sum, item) => sum + (item.product.price * item.quantity), 0);
    
    this.cartSummarySubject.next({ totalItems, totalPrice });
  }

  // Reset cart state (useful for logout)
  resetCartState(): void {
    this.cartItemsSubject.next([]);
    this.cartSummarySubject.next({ totalItems: 0, totalPrice: 0 });
  }
}
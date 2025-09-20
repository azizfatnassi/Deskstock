import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap, map } from 'rxjs/operators';
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
    return this.http.get<any[]>(this.baseUrl, { params })
      .pipe(
        map(items => items.map(item => this.mapBackendToFrontend(item))),
        tap(mapped => {
          this.cartItemsSubject.next(mapped);
          this.updateCartSummary(mapped);
        })
      );
  }

  // Add item to cart
  addToCart(cartItem: CartItemRequest): Observable<CartItemResponse> {
    console.log('=== CART SERVICE DEBUG START ===');
    const currentUser = this.authService.getCurrentUser();
    console.log('Current user in cart service:', currentUser);
    
    if (!currentUser || !currentUser.id || currentUser.id === 0) {
      console.error('User not authenticated in cart service');
      throw new Error('User not authenticated');
    }
    
    if (!cartItem || !cartItem.productId || !cartItem.quantity) {
      console.error('Invalid cart item data:', cartItem);
      throw new Error('Invalid cart item data');
    }
    
    // Create query parameters (backend expects @RequestParam)
    const params = new HttpParams()
      .set('userId', (currentUser.id || 0).toString())
      .set('productId', (cartItem.productId || 0).toString())
      .set('quantity', (cartItem.quantity || 1).toString());
    
    console.log('Query parameters:');
    console.log('- userId:', (currentUser.id || 0).toString());
    console.log('- productId:', (cartItem.productId || 0).toString());
    console.log('- quantity:', (cartItem.quantity || 1).toString());
    console.log('Making POST request to:', `${this.baseUrl}/add`);
    console.log('=== CART SERVICE DEBUG END ===');
    
    return this.http.post<CartItemResponse>(`${this.baseUrl}/add`, null, { params })
      .pipe(
        tap(() => this.refreshCartItems())
      );
  }

  // Update cart item quantity
  updateCartItem(productId: number, quantity: number): Observable<CartItemResponse> {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser || !currentUser.id || currentUser.id === 0) {
      throw new Error('User not authenticated');
    }

    const params = new HttpParams()
      .set('userId', (currentUser.id || 0).toString())
      .set('productId', (productId || 0).toString())
      .set('quantity', (quantity || 1).toString());

    return this.http.put<CartItemResponse>(`${this.baseUrl}/update`, null, { params })
      .pipe(
        tap(() => this.refreshCartItems())
      );
  }

  // Remove item from cart
  removeFromCart(productId: number): Observable<void> {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser || !currentUser.id || currentUser.id === 0) {
      throw new Error('User not authenticated');
    }

    const params = new HttpParams()
      .set('userId', (currentUser.id || 0).toString())
      .set('productId', (productId || 0).toString());

    return this.http.delete<void>(`${this.baseUrl}/remove`, { params })
      .pipe(
        tap(() => this.refreshCartItems())
      );
  }

  // Clear entire cart
  clearCart(): Observable<void> {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser || !currentUser.id || currentUser.id === 0) {
      throw new Error('User not authenticated');
    }

    const params = new HttpParams().set('userId', (currentUser.id || 0).toString());

    return this.http.delete<void>(`${this.baseUrl}/clear`, { params })
      .pipe(
        tap(() => {
          this.cartItemsSubject.next([]);
          this.cartSummarySubject.next({ totalItems: 0, totalPrice: 0 });
        })
      );
  }

  // Get cart summary (total items and price)
  getCartSummary(): Observable<CartSummary> {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser || !currentUser.id || currentUser.id === 0) {
      return new Observable(observer => {
        observer.next({ totalItems: 0, totalPrice: 0 });
        observer.complete();
      });
    }

    const params = new HttpParams().set('userId', (currentUser.id || 0).toString());

    return this.http.get<any>(`${this.baseUrl}/summary`, { params })
      .pipe(
        map(res => ({
          totalItems: Number(res?.totalItems ?? 0),
          totalPrice: Number(res?.totalPrice ?? 0)
        }) as CartSummary),
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
    return items.some(item => item.product && item.product.productId === productId);
  }

  // Get cart item by product ID
  getCartItemByProductId(productId: number): CartItemResponse | undefined {
    const items = this.cartItemsSubject.value;
    return items.find(item => item.product && item.product.productId === productId);
  }

  // Add or update cart item (convenience method)
  addOrUpdateCartItem(productId: number, quantity: number): Observable<CartItemResponse> {
    const existingItem = this.getCartItemByProductId(productId);
    
    if (existingItem) {
      // Update existing item
      const newQuantity = existingItem.quantity + quantity;
      return this.updateCartItem(existingItem.product.productId, newQuantity);
    } else {
      // Add new item
      return this.addToCart({ productId, quantity });
    }
  }

  // Private helper methods
  private refreshCartItems(): void {
    this.loadCartItems().subscribe();
  }

  public calculateTotalPrice(items: CartItemResponse[]): number {
    return items.reduce((sum, item) => {
      // Add null checks for item.product and item.product.price
      if (item.product && item.product.price && item.quantity) {
        return sum + (item.product.price * item.quantity);
      }
      return sum;
    }, 0);
  }

  private updateCartSummary(items: CartItemResponse[]): void {
    const totalItems = items.reduce((sum, item) => sum + (item.quantity || 0), 0);
    const totalPrice = this.calculateTotalPrice(items);
    
    this.cartSummarySubject.next({ totalItems, totalPrice });
  }

  // Reset cart state (useful for logout)
  resetCartState(): void {
    this.cartItemsSubject.next([]);
    this.cartSummarySubject.next({ totalItems: 0, totalPrice: 0 });
  }
  private mapBackendToFrontend(item: any): CartItemResponse {
    return {
      id: item.cartItemId ?? 0,
      product: {
        productId: item.productId ?? 0,
        name: item.productName ?? '',
        description: item.productDescription ?? '',
        price: Number(item.productPrice ?? 0),
        category: '',
        color: '',
        imageUrl: item.productImageUrl ?? '',
        stockQuantity: 0,
        isActive: true
      },
      quantity: item.quantity ?? 0
    };
  }
}
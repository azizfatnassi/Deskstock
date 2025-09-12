import { ProductResponse } from './product.model';
import { CartItem } from './cart-item.model';



export interface CartItemRequest {
  productId: number;
  quantity: number;
}

export interface CartItemResponse {
  id: number;
  product: ProductResponse;
  quantity: number;
}

export interface Cart {
  items: CartItem[];
  totalItems: number;
  totalPrice: number;
}
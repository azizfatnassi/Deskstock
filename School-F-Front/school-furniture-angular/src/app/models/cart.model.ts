import { ProductResponse } from './product.model';

export interface CartItem {
  id: number;
  product: ProductResponse;
  quantity: number;
  userId: number;
}

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

export interface CartSummary {
  totalItems: number;
  totalPrice: number;
}
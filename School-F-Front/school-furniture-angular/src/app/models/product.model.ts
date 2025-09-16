export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  category: string;
  color?: string;
  codeArticle?: string;
  imageUrl?: string;
  stockQuantity: number;
  isActive: boolean;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface ProductResponse {
  productId: number;
  name: string;
  description: string;
  price: number;
  category: string;
  color?: string;
  codeArticle?: string;
  imageUrl?: string;
  stockQuantity: number;
  isActive: boolean;
}

export interface ProductRequest {
  name: string;
  description: string;
  price: number;
  category: string;
  color?: string;
  codeArticle?: string;
  imageUrl?: string;
  stockQuantity: number;
}

// Removed enums - now using string fields
// Categories and colors are now free text fields

export interface CategoryResponse {
  name: string;
  displayName: string;
}

export interface ColorResponse {
  name: string;
  displayName: string;
}

export interface PagedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}
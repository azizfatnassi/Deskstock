export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  category: Category;
  color: Color;
  imageUrl?: string;
  stockQuantity: number;
  isActive: boolean;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface ProductResponse {
  id: number;
  name: string;
  description: string;
  price: number;
  category: string;
  color: string;
  imageUrl?: string;
  stockQuantity: number;
  isActive: boolean;
}

export interface ProductRequest {
  name: string;
  description: string;
  price: number;
  category: Category;
  color: Color;
  imageUrl?: string;
  stockQuantity: number;
}

export enum Category {
  DESKS = 'DESKS',
  CHAIRS = 'CHAIRS',
  TABLES = 'TABLES',
  STORAGE = 'STORAGE',
  ACCESSORIES = 'ACCESSORIES'
}

export enum Color {
  RED = 'RED',
  BLUE = 'BLUE',
  GREEN = 'GREEN',
  YELLOW = 'YELLOW',
  BLACK = 'BLACK',
  WHITE = 'WHITE',
  BROWN = 'BROWN',
  GRAY = 'GRAY'
}

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
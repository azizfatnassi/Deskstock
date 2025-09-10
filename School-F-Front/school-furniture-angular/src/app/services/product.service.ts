import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { 
  Product, 
  ProductResponse, 
  ProductRequest, 
  PagedResponse, 
  Category, 
  Color,
  CategoryResponse,
  ColorResponse
} from '../models';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private readonly baseUrl = 'http://localhost:8081/api';

  constructor(private http: HttpClient) {}

  // Get all products with pagination
  getAllProducts(page: number = 0, size: number = 12, sortBy: string = 'name', sortDir: string = 'asc'): Observable<PagedResponse<ProductResponse>> {
    const params = new HttpParams()
      .set('page', (page ?? 0).toString())
      .set('size', (size ?? 12).toString())
      .set('sortBy', sortBy || 'name')
      .set('sortDir', sortDir || 'asc');
    
    return this.http.get<PagedResponse<ProductResponse>>(`${this.baseUrl}/products`, { params });
  }

  // Get product by ID
  getProductById(id: number): Observable<ProductResponse> {
    return this.http.get<ProductResponse>(`${this.baseUrl}/products/${id}`);
  }

  // Search products
  searchProducts(query: string, page: number = 0, size: number = 12, sortBy: string = 'name', sortDir: string = 'asc'): Observable<PagedResponse<ProductResponse>> {
    const params = new HttpParams()
      .set('query', query || '')
      .set('page', (page ?? 0).toString())
      .set('size', (size ?? 12).toString())
      .set('sortBy', sortBy || 'name')
      .set('sortDir', sortDir || 'asc');
    
    return this.http.get<PagedResponse<ProductResponse>>(`${this.baseUrl}/products/search`, { params });
  }

  // Filter products by category
  getProductsByCategory(category: Category, page: number = 0, size: number = 12, sortBy: string = 'name', sortDir: string = 'asc'): Observable<PagedResponse<ProductResponse>> {
    const params = new HttpParams()
      .set('category', category || '')
      .set('page', (page ?? 0).toString())
      .set('size', (size ?? 12).toString())
      .set('sortBy', sortBy || 'name')
      .set('sortDir', sortDir || 'asc');
    
    return this.http.get<PagedResponse<ProductResponse>>(`${this.baseUrl}/products/filter/category`, { params });
  }

  // Filter products by color
  getProductsByColor(color: Color, page: number = 0, size: number = 12, sortBy: string = 'name', sortDir: string = 'asc'): Observable<PagedResponse<ProductResponse>> {
    const params = new HttpParams()
      .set('color', color || '')
      .set('page', (page ?? 0).toString())
      .set('size', (size ?? 12).toString())
      .set('sortBy', sortBy || 'name')
      .set('sortDir', sortDir || 'asc');
    
    return this.http.get<PagedResponse<ProductResponse>>(`${this.baseUrl}/products/filter/color`, { params });
  }

  // Filter products by price range
  getProductsByPriceRange(minPrice: number, maxPrice: number, page: number = 0, size: number = 12, sortBy: string = 'name', sortDir: string = 'asc'): Observable<PagedResponse<ProductResponse>> {
    const params = new HttpParams()
      .set('minPrice', (minPrice ?? 0).toString())
      .set('maxPrice', (maxPrice ?? 999999).toString())
      .set('page', (page ?? 0).toString())
      .set('size', (size ?? 12).toString())
      .set('sortBy', sortBy || 'name')
      .set('sortDir', sortDir || 'asc');
    
    return this.http.get<PagedResponse<ProductResponse>>(`${this.baseUrl}/products/filter/price`, { params });
  }

  // Advanced search with multiple filters
  searchProductsWithFilters(
    query?: string,
    category?: string,
    color?: string,
    minPrice?: number,
    maxPrice?: number,
    page: number = 0,
    size: number = 12,
    sortBy: string = 'name',
    sortDir: string = 'asc'
  ): Observable<PagedResponse<ProductResponse>> {
    let params = new HttpParams()
      .set('page', (page ?? 0).toString())
      .set('size', (size ?? 12).toString())
      .set('sortBy', sortBy || 'name')
      .set('sortDir', sortDir || 'asc');

    if (query && query.trim()) {
      params = params.set('query', query.trim());
    }
    if (category && category.trim()) {
      params = params.set('category', category.trim());
    }
    if (color && color.trim()) {
      params = params.set('color', color.trim());
    }
    if (minPrice !== null && minPrice !== undefined) {
      params = params.set('minPrice', (minPrice ?? 0).toString());
    }
    if (maxPrice !== null && maxPrice !== undefined) {
      params = params.set('maxPrice', (maxPrice ?? 999999).toString());
    }

    return this.http.get<PagedResponse<ProductResponse>>(`${this.baseUrl}/products/filter`, { params });
  }

  // Get recent products
  getRecentProducts(limit: number = 10): Observable<ProductResponse[]> {
    const params = new HttpParams().set('limit', (limit ?? 10).toString());
    return this.http.get<ProductResponse[]>(`${this.baseUrl}/products/recent`, { params });
  }

  // Get popular products
  getPopularProducts(limit: number = 10): Observable<ProductResponse[]> {
    const params = new HttpParams().set('limit', (limit ?? 10).toString());
    return this.http.get<ProductResponse[]>(`${this.baseUrl}/products/popular`, { params });
  }

  // Admin methods (require authentication)
  createProduct(product: ProductRequest): Observable<ProductResponse> {
    return this.http.post<ProductResponse>(`${this.baseUrl}/products`, product);
  }

  updateProduct(id: number, product: ProductRequest): Observable<ProductResponse> {
    return this.http.put<ProductResponse>(`${this.baseUrl}/products/${id}`, product);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/products/${id}`);
  }

  updateStock(id: number, quantity: number): Observable<ProductResponse> {
    const params = new HttpParams().set('quantity', (quantity ?? 0).toString());
    return this.http.patch<ProductResponse>(`${this.baseUrl}/products/${id}/stock`, null, { params });
  }

  checkAvailability(id: number, quantity: number): Observable<boolean> {
    const params = new HttpParams().set('quantity', (quantity ?? 1).toString());
    return this.http.get<boolean>(`${this.baseUrl}/products/${id}/availability`, { params });
  }

  // Get all categories
  getAllCategories(): Observable<CategoryResponse[]> {
    return this.http.get<CategoryResponse[]>(`${this.baseUrl}/categories`);
  }

  // Get category by name
  getCategoryByName(name: string): Observable<CategoryResponse> {
    return this.http.get<CategoryResponse>(`${this.baseUrl}/categories/${name}`);
  }

  // Get all colors
  getAllColors(): Observable<ColorResponse[]> {
    return this.http.get<ColorResponse[]>(`${this.baseUrl}/colors`);
  }

  // Get color by name
  getColorByName(name: string): Observable<ColorResponse> {
    return this.http.get<ColorResponse>(`${this.baseUrl}/colors/${name}`);
  }
}
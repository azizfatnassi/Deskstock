import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil, debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';
import { AuthService } from '../../services/auth.service';
import { Product, ProductResponse, PagedResponse } from '../../models/product.model';
import { CartItemRequest } from '../../models/cart.model';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  
  products: ProductResponse[] = [];
  categories: {name: string, displayName: string}[] = [];
  colors: {name: string, displayName: string}[] = [];
  
  // Search and filter parameters
  searchQuery: string = '';
  selectedCategory: string = '';
  selectedColor: string = '';
  minPrice: number | null = null;
  maxPrice: number | null = null;
  sortBy: string = 'name';
  sortDir: string = 'asc';
  page: number = 0;
  size: number = 12;
  
  // Pagination
  totalElements = 0;
  totalPages = 0;
  currentPage = 0;
  
  // UI state
  loading = false;
  error: string | null = null;
  addingToCart: { [productId: number]: boolean } = {};
  
  constructor(
    private productService: ProductService,
    private cartService: CartService,
    public authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {}
  
  ngOnInit(): void {
    this.handleRouteParams();
    this.loadCategories();
    this.loadColors();
  }
  
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
  
  private handleRouteParams(): void {
    this.route.queryParams.pipe(
      takeUntil(this.destroy$)
    ).subscribe(params => {
      if (params['category']) {
        // Find category by name and set selectedCategory
        const category = this.categories.find(c => c.name.toLowerCase() === params['category'].toLowerCase());
        if (category) {
          this.selectedCategory = category.name;
        }
      }
      if (params['search']) {
        this.searchQuery = params['search'];
      }
      this.searchProducts();
    });
  }
  
  private loadCategories(): void {
    // Load categories from database
    this.productService.getDistinctCategories().pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (categoryStrings: string[]) => {
        this.categories = categoryStrings.map(cat => ({
          name: cat,
          displayName: cat
        }));
      },
      error: (error) => {
        console.error('Error loading categories:', error);
        // Fallback to predefined categories
        const categoryStrings = this.productService.getPredefinedCategories();
        this.categories = categoryStrings.map(cat => ({
          name: cat,
          displayName: cat
        }));
      }
    });
  }

  private loadColors(): void {
    // Load colors from database
    this.productService.getDistinctColors().pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (colorStrings: string[]) => {
        this.colors = colorStrings.map(color => ({
          name: color,
          displayName: color
        }));
      },
      error: (error) => {
        console.error('Error loading colors:', error);
        // Fallback to predefined colors
        const colorStrings = this.productService.getPredefinedColors();
        this.colors = colorStrings.map(color => ({
          name: color,
          displayName: color
        }));
      }
    });
  }
  
  searchProducts(): void {
    this.loading = true;
    this.error = null;
    
    // Use the new advanced filter method that combines all filters
    const observable = this.productService.searchProductsWithFilters(
      this.searchQuery || undefined,
      this.selectedCategory || undefined,
      this.selectedColor || undefined,
      this.minPrice || undefined,
      this.maxPrice || undefined,
      this.page,
      this.size,
      this.sortBy,
      this.sortDir
    );
    
    observable.pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (response: PagedResponse<ProductResponse>) => {
        console.log('Products loaded:', response.content);
        if (response && response.content) {
          this.products = response.content;
          this.totalElements = response.totalElements;
          this.totalPages = response.totalPages;
          this.currentPage = response.number;
        } else {
          console.error('Invalid response format:', response);
          this.products = [];
          this.totalElements = 0;
          this.totalPages = 0;
          this.currentPage = 0;
        }
        this.loading = false;
        this.error = null;
      },
      error: (error: any) => {
        this.error = 'Error loading products. Please try again.';
        this.loading = false;
        console.error('Error searching products:', error);
        this.products = [];
      }
    });
  }
  
  onSearchChange(keyword: string): void {
    this.searchQuery = keyword;
    this.page = 0;
    this.searchProducts();
  }
  
  onCategoryChange(category: string): void {
    this.selectedCategory = category;
    this.page = 0;
    this.searchProducts();
  }
  
  onColorChange(color: string): void {
    this.selectedColor = color;
    this.page = 0;
    this.searchProducts();
  }
  
  onPriceRangeChange(minPrice: number | null, maxPrice: number | null): void {
    this.minPrice = minPrice;
    this.maxPrice = maxPrice;
    this.page = 0;
    this.searchProducts();
  }
  
  onPageChange(page: number): void {
    this.page = page;
    this.searchProducts();
  }
  
  clearFilters(): void {
    this.searchQuery = '';
    this.selectedCategory = '';
    this.selectedColor = '';
    this.minPrice = null;
    this.maxPrice = null;
    this.page = 0;
    this.searchProducts();
  }
  
  viewProduct(productId: number): void {
    this.router.navigate(['/products', productId]);
  }

  onSortChange(sortBy: string): void {
    this.sortBy = sortBy;
    this.sortDir = 'asc'; // Default to ascending
    this.page = 0;
    this.currentPage = 0;
    this.searchProducts();
  }



  addToCart(product: ProductResponse): void {
    console.log('=== ADD TO CART DEBUG START ===');
    console.log('Product:', product);
    
    if (!product || !product.productId) {
      console.error('Invalid product:', product);
      this.showAddToCartError();
      return;
    }

    if (product.stockQuantity <= 0) {
      this.showAddToCartError();
      return;
    }

    // Check if product is already being added to cart
    if (this.addingToCart[product.productId]) {
      console.log('Product already being added to cart');
      return;
    }

    // Debug authentication state
    console.log('=== AUTHENTICATION DEBUG ===');
    const currentUser = this.authService.getCurrentUser();
    const isAuthenticated = this.authService.isAuthenticated();
    const token = this.authService.getToken();
    const userFromStorage = localStorage.getItem('current_user');
    
    console.log('Current user from auth service:', currentUser);
    console.log('Is authenticated:', isAuthenticated);
    console.log('Token from auth service:', token);
    console.log('User from localStorage:', userFromStorage);
    
    if (userFromStorage) {
      try {
        const parsedUser = JSON.parse(userFromStorage);
        console.log('Parsed user from localStorage:', parsedUser);
      } catch (e) {
        console.error('Error parsing user from localStorage:', e);
      }
    }
    
    if (!currentUser) {
      console.log('No current user, redirecting to login');
      alert('Please log in to add items to cart');
      this.router.navigate(['/login']);
      return;
    }
    
    console.log('User ID for cart:', currentUser.id);
    console.log('=== AUTHENTICATION DEBUG END ===');

    this.addingToCart[product.productId] = true;

    const cartItem: CartItemRequest = {
      productId: product.productId,
      quantity: 1
    };

    console.log('Cart item to be sent:', cartItem);
    console.log('=== CALLING CART SERVICE ===');

    this.cartService.addToCart(cartItem).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: () => {
        this.addingToCart[product.productId] = false;
        // Show success message or animation
        this.showAddToCartSuccess(product.name);
      },
      error: (error) => {
        this.addingToCart[product.productId] = false;
        console.error('Error adding to cart:', error);
        // Show error message
        this.showAddToCartError();
      }
    });
  }

  private showAddToCartSuccess(productName: string): void {
    // Simple user-visible notification per your request
    alert('Product added to cart successfully!');
  }

  private showAddToCartError(): void {
    // Simple error alert
    alert('Failed to add item to cart. Please try again.');
  }

  getVisiblePages(): number[] {
    const visiblePages: number[] = [];
    const maxVisiblePages = 5; // Show 5 page numbers at most
    const halfVisible = Math.floor(maxVisiblePages / 2);
    
    let startPage = Math.max(0, this.currentPage - halfVisible);
    let endPage = Math.min(this.totalPages - 1, startPage + maxVisiblePages - 1);
    
    // Adjust start page if we're near the end
    if (endPage - startPage + 1 < maxVisiblePages) {
      startPage = Math.max(0, endPage - maxVisiblePages + 1);
    }
    
    for (let i = startPage; i <= endPage; i++) {
      visiblePages.push(i);
    }
    
    return visiblePages;
  }
}

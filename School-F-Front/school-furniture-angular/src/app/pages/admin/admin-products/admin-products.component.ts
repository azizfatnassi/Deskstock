import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil, debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { ProductService } from '../../../services/product.service';
import { ProductResponse, ProductRequest } from '../../../models/product.model';

@Component({
  selector: 'app-admin-products',
  templateUrl: './admin-products.component.html',
  styleUrls: ['./admin-products.component.css']
})
export class AdminProductsComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  
  // Data properties
  products: ProductResponse[] = [];
  filteredProducts: ProductResponse[] = [];
  paginatedProducts: ProductResponse[] = [];
  
  // UI state
  isLoading = false;
  isCreating = false;
  isUpdating = false;
  isDeleting = false;
  deletingProductId: number | null = null;
  
  // Messages
  successMessage = '';
  error = '';
  
  // Search and filters
  searchTerm = '';
  selectedCategory = '';
  
  // Pagination
  currentPage = 1;
  itemsPerPage = 10;
  totalPages = 0;
  
  // Modal states
  showAddModal = false;
  showEditModal = false;
  
  // Forms
  addProductForm!: FormGroup;
  editProductForm!: FormGroup;
  selectedProduct: ProductResponse | null = null;
  
  // Categories and colors loaded from database
  categories: string[] = [];
  colors: string[] = [];
  
  constructor(
    private productService: ProductService,
    private formBuilder: FormBuilder,
    private router: Router
  ) {
    this.initializeForms();
  }
  
  ngOnInit(): void {
    this.loadProducts();
    this.loadCategories();
    this.loadColors();
    this.setupSearch();
  }
  
  private loadCategories(): void {
    // Load categories from database
    this.productService.getDistinctCategories().pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (categories: string[]) => {
        this.categories = categories;
      },
      error: (error) => {
        console.error('Error loading categories:', error);
        // Fallback to predefined categories
        this.categories = this.productService.getPredefinedCategories();
      }
    });
  }
  
  private loadColors(): void {
    // Load colors from database
    this.productService.getDistinctColors().pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (colors: string[]) => {
        this.colors = colors;
      },
      error: (error) => {
        console.error('Error loading colors:', error);
        // Fallback to predefined colors
        this.colors = this.productService.getPredefinedColors();
      }
    });
  }
  
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
  
  private initializeForms(): void {
    this.addProductForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      price: ['', [Validators.required, Validators.min(0.01)]],
      category: ['', Validators.required],
      color: [''],
      codeArticle: [''],
      stockQuantity: ['', [Validators.required, Validators.min(0)]],
      imageUrl: ['']
    });
    
    this.editProductForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      price: ['', [Validators.required, Validators.min(0.01)]],
      category: ['', Validators.required],
      color: [''],
      codeArticle: [''],
      stockQuantity: ['', [Validators.required, Validators.min(0)]],
      imageUrl: ['']
    });
  }
  
  private setupSearch(): void {
    // Setup debounced search
    const searchSubject = new Subject<string>();
    searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      takeUntil(this.destroy$)
    ).subscribe(() => {
      this.applyFilters();
    });
    
    // Store reference for use in template
    (this as any).searchSubject = searchSubject;
  }
  
  // Data loading methods
  loadProducts(): void {
    this.isLoading = true;
    this.clearMessages();
    
    this.productService.getAllProducts(0, 1000).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (response) => {
        this.products = response.content || [];
        this.applyFilters();
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading products:', error);
        this.error = 'Failed to load products. Please try again.';
        this.isLoading = false;
      }
    });
  }
  
  // Search and filter methods
  onSearchChange(): void {
    if ((this as any).searchSubject) {
      (this as any).searchSubject.next(this.searchTerm);
    }
  }
  
  onCategoryFilterChange(): void {
    this.applyFilters();
  }
  
  applyFilters(): void {
    let filtered = [...this.products];
    
    // Apply search filter
    if (this.searchTerm.trim()) {
      const searchLower = this.searchTerm.toLowerCase();
      filtered = filtered.filter(product => 
        product.name.toLowerCase().includes(searchLower) ||
        product.description.toLowerCase().includes(searchLower)
      );
    }
    
    // Apply category filter
    if (this.selectedCategory) {
      filtered = filtered.filter(product => product.category === this.selectedCategory);
    }
    
    this.filteredProducts = filtered;
    this.updatePagination();
  }
  
  // Pagination methods
  updatePagination(): void {
    this.totalPages = Math.ceil(this.filteredProducts.length / this.itemsPerPage);
    if (this.currentPage > this.totalPages) {
      this.currentPage = 1;
    }
    this.updatePaginatedProducts();
  }
  
  updatePaginatedProducts(): void {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedProducts = this.filteredProducts.slice(startIndex, endIndex);
  }
  
  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.updatePaginatedProducts();
    }
  }
  
  prevPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updatePaginatedProducts();
    }
  }
  
  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages && page !== this.currentPage) {
      this.currentPage = page;
      this.updatePaginatedProducts();
    }
  }
  
  getVisiblePages(): number[] {
    const visiblePages: number[] = [];
    const maxVisiblePages = 5; // Show 5 page numbers at most
    const halfVisible = Math.floor(maxVisiblePages / 2);
    
    let startPage = Math.max(1, this.currentPage - halfVisible);
    let endPage = Math.min(this.totalPages, startPage + maxVisiblePages - 1);
    
    // Adjust start page if we're near the end
    if (endPage - startPage + 1 < maxVisiblePages) {
      startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }
    
    for (let i = startPage; i <= endPage; i++) {
      visiblePages.push(i);
    }
    
    return visiblePages;
  }
  
  // CRUD operations
  showAddProduct(): void {
    this.addProductForm.reset();
    this.showAddModal = true;
    this.clearMessages();
  }
  
  cancelAdd(): void {
    this.showAddModal = false;
    this.addProductForm.reset();
  }
  
  createProduct(): void {
    if (this.addProductForm.valid && !this.isCreating) {
      this.isCreating = true;
      this.clearMessages();
      
      const formValue = this.addProductForm.value;
      const productData: ProductRequest = {
        name: formValue.name,
        description: formValue.description,
        price: parseFloat(formValue.price),
        category: formValue.category,
        color: formValue.color,
        codeArticle: formValue.codeArticle,
        imageUrl: formValue.imageUrl || undefined,
        stockQuantity: parseInt(formValue.stockQuantity)
      };
      
      this.productService.createProduct(productData).pipe(
        takeUntil(this.destroy$)
      ).subscribe({
        next: (response) => {
          this.successMessage = 'Product created successfully!';
          this.showAddModal = false;
          this.addProductForm.reset();
          this.loadProducts();
          this.isCreating = false;
          
          // Clear success message after 3 seconds
          setTimeout(() => {
            this.successMessage = '';
          }, 3000);
        },
        error: (error) => {
          console.error('Error creating product:', error);
          this.error = 'Failed to create product. Please try again.';
          this.isCreating = false;
          
          // Clear error message after 5 seconds
          setTimeout(() => {
            this.error = '';
          }, 5000);
        }
      });
    } else {
      this.markFormGroupTouched(this.addProductForm);
    }
  }
  
  editProduct(product: ProductResponse): void {
    this.selectedProduct = product;
    this.editProductForm.patchValue({
      name: product.name,
      description: product.description,
      price: product.price,
      category: product.category,
      color: product.color,
      codeArticle: product.codeArticle,
      stockQuantity: product.stockQuantity,
      imageUrl: product.imageUrl
    });
    this.showEditModal = true;
    this.clearMessages();
  }
  
  cancelEdit(): void {
    this.showEditModal = false;
    this.selectedProduct = null;
    this.editProductForm.reset();
  }
  
  updateProduct(): void {
    if (this.editProductForm.valid && this.selectedProduct && !this.isUpdating) {
      this.isUpdating = true;
      this.clearMessages();
      
      const formValue = this.editProductForm.value;
      const productData: ProductRequest = {
        name: formValue.name,
        description: formValue.description,
        price: parseFloat(formValue.price),
        category: formValue.category,
        color: formValue.color,
        codeArticle: formValue.codeArticle,
        imageUrl: formValue.imageUrl || undefined,
        stockQuantity: parseInt(formValue.stockQuantity)
      };
      
      this.productService.updateProduct(this.selectedProduct.productId, productData).pipe(
        takeUntil(this.destroy$)
      ).subscribe({
        next: (response) => {
          this.successMessage = 'Product updated successfully!';
          this.showEditModal = false;
          this.selectedProduct = null;
          this.editProductForm.reset();
          this.loadProducts();
          this.isUpdating = false;
          
          // Clear success message after 3 seconds
          setTimeout(() => {
            this.successMessage = '';
          }, 3000);
        },
        error: (error) => {
          console.error('Error updating product:', error);
          this.error = 'Failed to update product. Please try again.';
          this.isUpdating = false;
          
          // Clear error message after 5 seconds
          setTimeout(() => {
            this.error = '';
          }, 5000);
        }
      });
    } else {
      this.markFormGroupTouched(this.editProductForm);
    }
  }
  
  deleteProduct(product: ProductResponse): void {
    if (confirm(`Are you sure you want to delete "${product.name}"? This action cannot be undone.`)) {
      this.isDeleting = true;
      this.deletingProductId = product.productId;
      this.clearMessages();
      
      this.productService.deleteProduct(product.productId).pipe(
        takeUntil(this.destroy$)
      ).subscribe({
        next: () => {
          this.successMessage = 'Product deleted successfully!';
          this.loadProducts();
          this.isDeleting = false;
          this.deletingProductId = null;
          
          // Clear success message after 3 seconds
          setTimeout(() => {
            this.successMessage = '';
          }, 3000);
        },
        error: (error) => {
          console.error('Error deleting product:', error);
          this.error = 'Failed to delete product. Please try again.';
          this.isDeleting = false;
          this.deletingProductId = null;
          
          // Clear error message after 5 seconds
          setTimeout(() => {
            this.error = '';
          }, 5000);
        }
      });
    }
  }
  
  // Navigation
  goBackToDashboard(): void {
    this.router.navigate(['/admin/dashboard']);
  }
  
  // Utility methods
  private clearMessages(): void {
    this.successMessage = '';
    this.error = '';
  }
  
  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();
    });
  }
  
  // Template helper methods
  getCategoryLabel(value: string): string {
    return value || 'N/A';
  }
  
  getColorLabel(value: string): string {
    return value || 'N/A';
  }
  
  isProductDeleting(productId: number): boolean {
    return this.isDeleting && this.deletingProductId === productId;
  }
}
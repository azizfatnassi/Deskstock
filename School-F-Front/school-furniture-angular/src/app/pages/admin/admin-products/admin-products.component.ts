import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { ProductService } from '../../../services/product.service';

interface ProductResponse {
  id?: number;
  name: string;
  description: string;
  price: number;
  stockQuantity: number;
  category: string;
  imageUrl?: string;
}

@Component({
  selector: 'app-admin-products',
  templateUrl: './admin-products.component.html',
  styleUrls: ['./admin-products.component.scss']
})
export class AdminProductsComponent implements OnInit {
  products: ProductResponse[] = [];
  filteredProducts: ProductResponse[] = [];
  currentUser: any;
  
  // Form and UI state
  productForm: FormGroup;
  showAddForm: boolean = false;
  editingProduct: ProductResponse | null = null;
  isLoading: boolean = true;
  isSubmitting: boolean = false;
  error: string = '';
  successMessage: string = '';
  
  // Search and filter
  searchTerm: string = '';
  selectedCategory: string = '';
  stockFilter: string = 'all'; // all, low, out
  
  // Pagination
  currentPage: number = 1;
  itemsPerPage: number = 10;
  totalPages: number = 1;
  
  categories: string[] = ['Chair', 'Table', 'Desk', 'Storage', 'Lighting', 'Decor'];
  
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private productService: ProductService,
    private router: Router
  ) {
    this.productForm = this.createProductForm();
  }
  
  ngOnInit(): void {
    this.checkAdminAccess();
    this.loadProducts();
  }
  
  checkAdminAccess(): void {
    this.currentUser = this.authService.getCurrentUser();
    if (!this.currentUser || !this.authService.isAdmin()) {
      this.router.navigate(['/admin/login']);
    }
  }
  
  createProductForm(): FormGroup {
    return this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      price: ['', [Validators.required, Validators.min(0.01)]],
      stockQuantity: ['', [Validators.required, Validators.min(0)]],
      category: ['', Validators.required],
      imageUrl: ['']
    });
  }
  
  loadProducts(): void {
    this.isLoading = true;
    this.error = '';
    
    this.productService.getAllProducts().subscribe({
      next: (products) => {
        this.products = products.content || [];
        this.applyFilters();
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading products:', error);
        this.error = 'Failed to load products';
        this.isLoading = false;
      }
    });
  }
  
  applyFilters(): void {
    let filtered = [...this.products];
    
    // Search filter
    if (this.searchTerm) {
      filtered = filtered.filter(product => 
        product.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        product.description.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    }
    
    // Category filter
    if (this.selectedCategory) {
      filtered = filtered.filter(product => product.category === this.selectedCategory);
    }
    
    // Stock filter
    if (this.stockFilter === 'low') {
      filtered = filtered.filter(product => product.stockQuantity > 0 && product.stockQuantity <= 10);
    } else if (this.stockFilter === 'out') {
      filtered = filtered.filter(product => product.stockQuantity === 0);
    }
    
    this.filteredProducts = filtered;
    this.totalPages = Math.ceil((this.filteredProducts?.length || 0) / this.itemsPerPage);
    this.currentPage = 1;
  }
  
  getPaginatedProducts(): ProductResponse[] {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    return this.filteredProducts.slice(startIndex, endIndex);
  }
  
  onSearchChange(): void {
    this.applyFilters();
  }
  
  onCategoryChange(): void {
    this.applyFilters();
  }
  
  onStockFilterChange(): void {
    this.applyFilters();
  }
  
  showAddProductForm(): void {
    this.showAddForm = true;
    this.editingProduct = null;
    this.productForm.reset();
    this.clearMessages();
  }
  
  editProduct(product: ProductResponse): void {
    this.editingProduct = product;
    this.showAddForm = true;
    this.productForm.patchValue({
      name: product.name,
      description: product.description,
      price: product.price,
      stockQuantity: product.stockQuantity,
      category: product.category,
      imageUrl: product.imageUrl
    });
    this.clearMessages();
  }
  
  cancelForm(): void {
    this.showAddForm = false;
    this.editingProduct = null;
    this.productForm.reset();
    this.clearMessages();
  }
  
  onSubmit(): void {
    if (this.productForm.valid) {
      this.isSubmitting = true;
      this.clearMessages();
      
      const productData = this.productForm.value;
      
      if (this.editingProduct) {
        // Update existing product
        this.productService.updateProduct(this.editingProduct.id!, productData).subscribe({
          next: (updatedProduct) => {
            this.successMessage = 'Product updated successfully!';
            this.loadProducts();
            this.cancelForm();
            this.isSubmitting = false;
          },
          error: (error: any) => {
            console.error('Error updating product:', error);
            this.error = 'Failed to update product';
            this.isSubmitting = false;
          }
        });
      } else {
        // Create new product
        this.productService.createProduct(productData).subscribe({
          next: (newProduct) => {
            this.successMessage = 'Product created successfully!';
            this.loadProducts();
            this.cancelForm();
            this.isSubmitting = false;
          },
          error: (error) => {
            console.error('Error creating product:', error);
            this.error = 'Failed to create product';
            this.isSubmitting = false;
          }
        });
      }
    } else {
      this.markFormGroupTouched();
    }
  }
  
  deleteProduct(product: ProductResponse): void {
    if (confirm(`Are you sure you want to delete "${product.name}"?`)) {
      this.productService.deleteProduct(product.id!).subscribe({
        next: () => {
          this.successMessage = 'Product deleted successfully!';
          this.loadProducts();
        },
        error: (error) => {
          console.error('Error deleting product:', error);
          this.error = 'Failed to delete product';
        }
      });
    }
  }
  
  updateStock(product: ProductResponse, newStock: number): void {
    if (newStock >= 0) {
      this.productService.updateStock(product.id!, newStock).subscribe({
        next: () => {
          product.stockQuantity = newStock;
          this.successMessage = 'Stock updated successfully!';
        },
        error: (error) => {
          console.error('Error updating stock:', error);
          this.error = 'Failed to update stock';
        }
      });
    }
  }
  
  markFormGroupTouched(): void {
    Object.keys(this.productForm.controls).forEach(key => {
      this.productForm.get(key)?.markAsTouched();
    });
  }
  
  isFieldInvalid(fieldName: string): boolean {
    const field = this.productForm.get(fieldName);
    return !!(field && field.invalid && field.touched);
  }
  
  getFieldError(fieldName: string): string {
    const field = this.productForm.get(fieldName);
    if (field?.errors) {
      if (field.errors['required']) return `${fieldName} is required`;
      if (field.errors['minlength']) return `${fieldName} is too short`;
      if (field.errors['min']) return `${fieldName} must be greater than 0`;
    }
    return '';
  }
  
  clearMessages(): void {
    this.error = '';
    this.successMessage = '';
  }
  
  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
    }
  }
  
  getStockStatus(stockQuantity: number): string {
    if (stockQuantity === 0) return 'out-of-stock';
    if (stockQuantity <= 10) return 'low-stock';
    return 'in-stock';
  }
  
  getStockLabel(stockQuantity: number): string {
    if (stockQuantity === 0) return 'Out of Stock';
    if (stockQuantity <= 10) return 'Low Stock';
    return 'In Stock';
  }
  
  navigateToDashboard(): void {
    this.router.navigate(['/admin/dashboard']);
  }
}

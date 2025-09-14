import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { UserService } from '../../../services/user.service';

interface User {
  userId: number;
  name: string;
  email: string;
  role: string;
  createdAt?: string;
}

@Component({
  selector: 'app-admin-users',
  templateUrl: './admin-users.component.html',
  styleUrls: ['./admin-users.component.scss']
})
export class AdminUsersComponent implements OnInit {
  users: User[] = [];
  filteredUsers: User[] = [];
  currentUser: any;
  
  // Pagination
  currentPage: number = 1;
  pageSize: number = 10;
  
  // Filters
  searchTerm: string = '';
  selectedRole: string = '';
  
  // Loading states
  isLoading: boolean = true;
  isUpdating: boolean = false;
  isDeleting: boolean = false;
  
  // Messages
  error: string = '';
  successMessage: string = '';
  
  // Modals
  showEditModal: boolean = false;
  showDeleteModal: boolean = false;
  
  // Forms and selected items
  editUserForm: FormGroup;
  selectedUser: User | null = null;
  userToDelete: User | null = null;
  
  constructor(
    private authService: AuthService,
    private userService: UserService,
    private router: Router,
    private formBuilder: FormBuilder
  ) {
    this.editUserForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      role: ['USER', [Validators.required]]
    });
  }
  
  ngOnInit(): void {
    this.loadUserData();
    this.loadUsers();
  }
  
  loadUserData(): void {
    this.currentUser = this.authService.getCurrentUser();
    if (!this.currentUser || !this.authService.isAdmin()) {
      this.router.navigate(['/admin/login']);
    }
  }
  
  loadUsers(): void {
    this.isLoading = true;
    this.error = '';
    
    this.userService.getAllUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.applyFilters();
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading users:', error);
        this.error = 'Failed to load users';
        this.isLoading = false;
      }
    });
  }
  
  applyFilters(): void {
    let filtered = [...this.users];
    
    // Apply search filter
    if (this.searchTerm.trim()) {
      const searchLower = this.searchTerm.toLowerCase();
      filtered = filtered.filter(user => 
        user.name.toLowerCase().includes(searchLower) ||
        user.email.toLowerCase().includes(searchLower)
      );
    }
    
    // Apply role filter
    if (this.selectedRole) {
      filtered = filtered.filter(user => user.role === this.selectedRole);
    }
    
    this.filteredUsers = filtered;
    this.currentPage = 1; // Reset to first page when filters change
  }
  
  onSearchChange(): void {
    this.applyFilters();
  }
  
  onRoleFilterChange(): void {
    this.applyFilters();
  }
  
  getPaginatedUsers(): User[] {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    return this.filteredUsers.slice(startIndex, endIndex);
  }
  
  getTotalPages(): number {
    return Math.ceil(this.filteredUsers.length / this.pageSize);
  }
  
  goToPage(page: number): void {
    if (page >= 1 && page <= this.getTotalPages()) {
      this.currentPage = page;
    }
  }
  
  editUser(user: User): void {
    this.selectedUser = user;
    this.editUserForm.patchValue({
      name: user.name,
      email: user.email,
      role: user.role
    });
    this.showEditModal = true;
  }
  
  updateUser(): void {
    if (this.editUserForm.valid && this.selectedUser) {
      this.isUpdating = true;
      const updatedUser = {
        ...this.selectedUser,
        ...this.editUserForm.value
      };
      
      this.userService.updateUser(this.selectedUser.userId, updatedUser).subscribe({
        next: (response) => {
          this.successMessage = 'User updated successfully';
          this.loadUsers();
          this.cancelEdit();
          this.isUpdating = false;
          
          // Clear success message after 3 seconds
          setTimeout(() => {
            this.successMessage = '';
          }, 3000);
        },
        error: (error) => {
          console.error('Error updating user:', error);
          this.error = 'Failed to update user';
          this.isUpdating = false;
        }
      });
    }
  }
  
  cancelEdit(): void {
    this.showEditModal = false;
    this.selectedUser = null;
    this.editUserForm.reset();
  }
  
  deleteUser(user: User): void {
    this.userToDelete = user;
    this.showDeleteModal = true;
  }
  
  confirmDelete(): void {
    if (this.userToDelete) {
      this.isDeleting = true;
      
      this.userService.deleteUser(this.userToDelete.userId).subscribe({
        next: () => {
          this.successMessage = 'User deleted successfully';
          this.loadUsers();
          this.cancelDelete();
          this.isDeleting = false;
          
          // Clear success message after 3 seconds
          setTimeout(() => {
            this.successMessage = '';
          }, 3000);
        },
        error: (error) => {
          console.error('Error deleting user:', error);
          this.error = 'Failed to delete user';
          this.isDeleting = false;
        }
      });
    }
  }
  
  cancelDelete(): void {
    this.showDeleteModal = false;
    this.userToDelete = null;
  }
  
  refreshUsers(): void {
    this.loadUsers();
  }
  
  navigateToDashboard(): void {
    this.router.navigate(['/admin/dashboard']);
  }
  
  formatDate(dateString: string | undefined): string {
    if (!dateString) return 'N/A';
    
    try {
      const date = new Date(dateString);
      return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
      });
    } catch {
      return 'N/A';
    }
  }
}
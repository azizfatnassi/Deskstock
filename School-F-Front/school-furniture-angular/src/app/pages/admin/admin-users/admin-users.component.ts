import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { UserService } from '../../../services/user.service';

interface User {
  userId: number;
  name: string;
  email: string;
  role: string;
  createdAt?: string;
}

interface CreateUserRequest {
  name: string;
  email: string;
  password: string;
  role: string;
}

interface UpdateUserRequest {
  name: string;
  email: string;
  role: string;
}

@Component({
  selector: 'app-admin-users',
  templateUrl: './admin-users.component.html',
  styleUrls: ['./admin-users.component.css']
})
export class AdminUsersComponent implements OnInit {
  users: User[] = [];
  filteredUsers: User[] = [];
  currentUser: any;
  
  // Pagination
  currentPage: number = 1;
  itemsPerPage: number = 10;
  totalPages: number = 1;
  
  // Search and filter
  searchTerm: string = '';
  selectedRole: string = '';
  
  // Form and UI state
  addUserForm!: FormGroup;
  editUserForm!: FormGroup;
  showAddModal: boolean = false;
  showEditModal: boolean = false;
  editingUser: User | null = null;
  isLoading: boolean = true;
  isCreating: boolean = false;
  isUpdating: boolean = false;
  isDeleting: boolean = false;
  error: string = '';
  successMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private userService: UserService,
    private router: Router
  ) {
    this.addUserForm = this.createUserForm();
    this.editUserForm = this.createUserForm();
  }

  ngOnInit(): void {
    this.checkAdminAccess();
    this.loadUsers();
  }

  checkAdminAccess(): void {
    this.currentUser = this.authService.getCurrentUser();
    if (!this.currentUser || !this.authService.isAdmin()) {
      this.router.navigate(['/admin/login']);
    }
  }

  goBackToDashboard(): void {
    this.router.navigate(['/admin/dashboard']);
  }

  createUserForm(): FormGroup {
    return this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      role: ['USER', Validators.required]
    });
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
    this.filteredUsers = this.users.filter(user => {
      const matchesSearch = !this.searchTerm || 
        user.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        user.email.toLowerCase().includes(this.searchTerm.toLowerCase());
      const matchesRole = !this.selectedRole || user.role === this.selectedRole;
      return matchesSearch && matchesRole;
    });
    this.updatePagination();
  }

  updatePagination(): void {
    this.totalPages = Math.ceil(this.filteredUsers.length / this.itemsPerPage);
    if (this.currentPage > this.totalPages) {
      this.currentPage = 1;
    }
  }

  get paginatedUsers(): User[] {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    return this.filteredUsers.slice(startIndex, endIndex);
  }

  onSearchChange(): void {
    this.applyFilters();
  }

  onRoleFilterChange(): void {
    this.applyFilters();
  }

  showAddUser(): void {
    this.showAddModal = true;
    this.addUserForm.reset();
    this.addUserForm.patchValue({ role: 'USER' });
    this.clearMessages();
  }

  editUser(user: User): void {
    this.editingUser = user;
    this.showEditModal = true;
    this.editUserForm.patchValue({
      name: user.name,
      email: user.email,
      role: user.role
    });
    this.clearMessages();
  }

  cancelAdd(): void {
    this.showAddModal = false;
    this.addUserForm.reset();
    this.clearMessages();
  }

  cancelEdit(): void {
    this.showEditModal = false;
    this.editingUser = null;
    this.editUserForm.reset();
    this.clearMessages();
  }

  createUser(): void {
    if (this.addUserForm.valid) {
      this.isCreating = true;
      this.clearMessages();
      
      const userData: CreateUserRequest = {
        name: this.addUserForm.value.name,
        email: this.addUserForm.value.email,
        password: this.addUserForm.value.password,
        role: this.addUserForm.value.role
      };
      
      this.userService.createUser(userData).subscribe({
        next: (newUser) => {
          this.successMessage = 'User created successfully!';
          this.loadUsers();
          this.cancelAdd();
          this.isCreating = false;
          setTimeout(() => this.clearMessages(), 3000);
        },
        error: (error) => {
          console.error('Error creating user:', error);
          this.error = 'Failed to create user';
          this.isCreating = false;
          setTimeout(() => this.clearMessages(), 5000);
        }
      });
    } else {
      this.markFormGroupTouched(this.addUserForm);
    }
  }

  updateUser(): void {
    if (this.editUserForm.valid && this.editingUser) {
      this.isUpdating = true;
      this.clearMessages();
      
      const userData: UpdateUserRequest = {
        name: this.editUserForm.value.name,
        email: this.editUserForm.value.email,
        role: this.editUserForm.value.role
      };
      
      this.userService.updateUser(this.editingUser.userId, userData).subscribe({
        next: (updatedUser) => {
          this.successMessage = 'User updated successfully!';
          this.loadUsers();
          this.cancelEdit();
          this.isUpdating = false;
          setTimeout(() => this.clearMessages(), 3000);
        },
        error: (error) => {
          console.error('Error updating user:', error);
          this.error = 'Failed to update user';
          this.isUpdating = false;
          setTimeout(() => this.clearMessages(), 5000);
        }
      });
    } else {
      this.markFormGroupTouched(this.editUserForm);
    }
  }

  deleteUser(user: User): void {
    if (confirm(`Are you sure you want to delete user "${user.name}"?`)) {
      this.isDeleting = true;
      this.clearMessages();
      
      this.userService.deleteUser(user.userId).subscribe({
        next: () => {
          this.successMessage = 'User deleted successfully!';
          this.loadUsers();
          this.isDeleting = false;
          setTimeout(() => this.clearMessages(), 3000);
        },
        error: (error) => {
          console.error('Error deleting user:', error);
          this.error = 'Failed to delete user';
          this.isDeleting = false;
          setTimeout(() => this.clearMessages(), 5000);
        }
      });
    }
  }

  markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();
    });
  }

  clearMessages(): void {
    this.error = '';
    this.successMessage = '';
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
    }
  }

  prevPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
    }
  }
}
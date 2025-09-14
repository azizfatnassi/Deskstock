import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

interface User {
  userId: number;
  name: string;
  email: string;
  role: string;
  createdAt?: string;
}

interface UpdateUserRequest {
  name: string;
  email: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = 'http://localhost:8081/api';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  /**
   * Get all users (Admin only)
   */
  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/users/admin/all`, {
      headers: this.getAuthHeaders()
    });
  }

  /**
   * Get user by ID
   */
  getUserById(userId: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/users/${userId}`, {
      headers: this.getAuthHeaders()
    });
  }

  /**
   * Update user (Admin only)
   */
  updateUser(userId: number, userData: UpdateUserRequest): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/admin/users/${userId}`, userData, {
      headers: this.getAuthHeaders()
    });
  }

  /**
   * Delete user (Admin only)
   */
  deleteUser(userId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/users/admin/${userId}`, {
      headers: this.getAuthHeaders()
    });
  }

  /**
   * Get user statistics
   */
  getUserStats(): Observable<any> {
    return this.http.get(`${this.baseUrl}/admin/users/stats`, {
      headers: this.getAuthHeaders()
    });
  }
}
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { 
  User, 
  UserResponse, 
  LoginRequest, 
  RegisterRequest, 
  AuthResponse, 
  UpdateProfileRequest, 
  ChangePasswordRequest 
} from '../models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly baseUrl = 'http://localhost:8081/api/users';
  private readonly tokenKey = 'auth_token';
  private readonly userKey = 'current_user';
  
  private currentUserSubject = new BehaviorSubject<UserResponse | null>(this.getCurrentUserFromStorage());
  public currentUser$ = this.currentUserSubject.asObservable();
  
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasValidToken());
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(private http: HttpClient) {}

  // Register new user
  register(registerData: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/register`, registerData)
      .pipe(
        tap(response => this.handleAuthSuccess(response))
      );
  }

  // Login user
  login(loginData: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, loginData)
      .pipe(
        tap(response => this.handleAuthSuccess(response))
      );
  }

  // Logout user
  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
    this.currentUserSubject.next(null);
    this.isAuthenticatedSubject.next(false);
  }

  // Get current user profile
  getCurrentUserProfile(): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.baseUrl}/profile`);
  }

  // Update user profile
  updateProfile(profileData: UpdateProfileRequest): Observable<UserResponse> {
    return this.http.put<UserResponse>(`${this.baseUrl}/profile`, profileData)
      .pipe(
        tap(user => {
          localStorage.setItem(this.userKey, JSON.stringify(user));
          this.currentUserSubject.next(user);
        })
      );
  }

  // Change password
  changePassword(passwordData: ChangePasswordRequest): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/change-password`, passwordData);
  }

  // Get authentication token
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  // Check if user is authenticated
  isAuthenticated(): boolean {
    return this.hasValidToken();
  }

  // Get current user
  getCurrentUser(): UserResponse | null {
    return this.currentUserSubject.value;
  }

  // Check if current user is admin
  isAdmin(): boolean {
    const user = this.getCurrentUser();
    return user?.role === 'ADMIN';
  }

  // Check if current user is regular user
  isUser(): boolean {
    const user = this.getCurrentUser();
    return user?.role === 'USER';
  }

  // Private helper methods
  private handleAuthSuccess(response: AuthResponse): void {
    localStorage.setItem(this.tokenKey, response.token);
    
    // Create user object from flat response with null checks
    const name = response.name || '';
    const nameParts = name.split(' ');
    
    const user: UserResponse = {
      id: response.userId || 0,
      username: name, // Use name as username
      email: response.email || '',
      firstName: nameParts[0] || '',
      lastName: nameParts.slice(1).join(' ') || '',
      role: response.role || 'USER'
    };
    
    console.log('Auth success - User object created:', user);
    localStorage.setItem(this.userKey, JSON.stringify(user));
    this.currentUserSubject.next(user);
    this.isAuthenticatedSubject.next(true);
  }

  private getCurrentUserFromStorage(): UserResponse | null {
    const userJson = localStorage.getItem(this.userKey);
    if (!userJson || userJson === 'undefined' || userJson === 'null') {
      return null;
    }
    
    try {
      return JSON.parse(userJson);
    } catch (error) {
      // Clear invalid user data
      localStorage.removeItem(this.userKey);
      return null;
    }
  }

  private hasValidToken(): boolean {
    const token = localStorage.getItem(this.tokenKey);
    if (!token || token === 'undefined' || token === 'null') {
      return false;
    }
    
    try {
      // Check if token has proper JWT format (3 parts separated by dots)
      const parts = token.split('.');
      if (parts.length !== 3) {
        return false;
      }
      
      // Basic token validation - decode payload and check expiration
      const payload = JSON.parse(atob(parts[1]));
      if (!payload.exp) {
        return true; // If no expiration, consider valid
      }
      
      const currentTime = Math.floor(Date.now() / 1000);
      return payload.exp > currentTime;
    } catch (error) {
      // Clear invalid token
      localStorage.removeItem(this.tokenKey);
      return false;
    }
  }
}
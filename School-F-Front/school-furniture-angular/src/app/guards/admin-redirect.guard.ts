import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AdminRedirectGuard implements CanActivate {
  
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}
  
  canActivate(): boolean {
    // If user is authenticated and is an admin, redirect to admin dashboard
    if (this.authService.isAuthenticated() && this.authService.isAdmin()) {
      this.router.navigate(['/admin/dashboard']);
      return false;
    }
    
    // Allow access for non-admin users or unauthenticated users
    return true;
  }
}
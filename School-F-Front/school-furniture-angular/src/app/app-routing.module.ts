import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { ProductsComponent } from './pages/products/products.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { CartComponent } from './pages/cart/cart.component';

import { AdminDashboardComponent } from './pages/admin/admin-dashboard/admin-dashboard.component';
import { AdminProductsComponent } from './pages/admin/admin-products/admin-products.component';
import { AdminUsersComponent } from './pages/admin/admin-users/admin-users.component';

import { AuthGuard } from './guards/auth.guard';
import { AdminGuard } from './guards/admin.guard';
import { AdminRedirectGuard } from './guards/admin-redirect.guard';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent, canActivate: [AdminRedirectGuard] },
  { path: 'products', component: ProductsComponent, canActivate: [AdminRedirectGuard] },
  { path: 'login', component: LoginComponent, canActivate: [AdminRedirectGuard] },
  { path: 'register', component: RegisterComponent, canActivate: [AdminRedirectGuard] },
  { path: 'cart', component: CartComponent, canActivate: [AuthGuard, AdminRedirectGuard] },
  
  // Admin routes
  { path: 'admin/dashboard', component: AdminDashboardComponent, canActivate: [AdminGuard] },
  { path: 'admin/products', component: AdminProductsComponent, canActivate: [AdminGuard] },
  { path: 'admin/users', component: AdminUsersComponent, canActivate: [AdminGuard] },
  { path: 'admin', redirectTo: '/admin/dashboard', pathMatch: 'full' },

  { path: '**', redirectTo: '/home' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

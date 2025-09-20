import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
//import { HomeComponent } from './pages/home/home.component';
import { ProductsComponent } from './pages/products/products.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { CartComponent } from './pages/cart/cart.component';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { OrderConfirmationComponent } from './components/order-confirmation/order-confirmation.component';
import { MyOrdersComponent } from './pages/my-orders/my-orders.component';
import { AdminOrdersComponent } from './pages/admin/admin-orders/admin-orders.component';

import { AdminDashboardComponent } from './pages/admin/admin-dashboard/admin-dashboard.component';
import { AdminProductsComponent } from './pages/admin/admin-products/admin-products.component';
import { AdminUsersComponent } from './pages/admin/admin-users/admin-users.component';
// AdminOrdersComponent import removed

import { AuthGuard } from './guards/auth.guard';
import { AdminGuard } from './guards/admin.guard';
import { AdminRedirectGuard } from './guards/admin-redirect.guard';

const routes: Routes = [
  { path: '', redirectTo: '/products', pathMatch: 'full' },
  { path: 'products', component: ProductsComponent, canActivate: [AdminRedirectGuard] },
  { path: 'login', component: LoginComponent, canActivate: [AdminRedirectGuard] },
  { path: 'register', component: RegisterComponent, canActivate: [AdminRedirectGuard] },
  { path: 'cart', component: CartComponent, canActivate: [AuthGuard, AdminRedirectGuard] },
  { path: 'checkout', component: CheckoutComponent, canActivate: [AuthGuard, AdminRedirectGuard] },
  { path: 'order-confirmation/:id', component: OrderConfirmationComponent, canActivate: [AuthGuard, AdminRedirectGuard] },
  { path: 'my-orders', component: MyOrdersComponent, canActivate: [AuthGuard, AdminRedirectGuard] },
  { path: 'admin/orders', component: AdminOrdersComponent, canActivate: [AuthGuard, AdminGuard] },
  
  // Admin routes
  { path: 'admin/dashboard', component: AdminDashboardComponent, canActivate: [AdminGuard] },
  { path: 'admin/products', component: AdminProductsComponent, canActivate: [AdminGuard] },
  { path: 'admin/users', component: AdminUsersComponent, canActivate: [AdminGuard] },
  // admin/orders route removed
  { path: 'admin', redirectTo: '/admin/dashboard', pathMatch: 'full' },

  { path: '**', redirectTo: '/products' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

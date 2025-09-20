import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthInterceptor } from './services';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { CartDropdownComponent } from './components/cart-dropdown/cart-dropdown.component';
//import { HomeComponent } from './pages/home/home.component';
import { ProductsComponent } from './pages/products/products.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { CartComponent } from './pages/cart/cart.component';
import { CheckoutComponent } from './components/checkout/checkout.component';
// OrderConfirmationComponent import removed
import { AdminDashboardComponent } from './pages/admin/admin-dashboard/admin-dashboard.component';
import { AdminProductsComponent } from './pages/admin/admin-products/admin-products.component';
import { AdminUsersComponent } from './pages/admin/admin-users/admin-users.component';
import { OrderConfirmationComponent } from './components/order-confirmation/order-confirmation.component';
import { MyOrdersComponent } from './pages/my-orders/my-orders.component';
import { AdminOrdersComponent } from './pages/admin/admin-orders/admin-orders.component';
// AdminOrdersComponent import removed


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    CartDropdownComponent,
    //HomeComponent,
    ProductsComponent,
    LoginComponent,
    RegisterComponent,
    CartComponent,
    CheckoutComponent,
    // OrderConfirmationComponent removed
    AdminDashboardComponent,
    AdminProductsComponent,
    AdminUsersComponent,
    OrderConfirmationComponent,
    MyOrdersComponent,
    AdminOrdersComponent,
    // AdminOrdersComponent removed
  ],
  imports: [
    BrowserModule,
    CommonModule,
    RouterModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  bootstrap: [AppComponent]
})
export class AppModule { }

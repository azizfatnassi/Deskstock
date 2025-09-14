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
import { HomeComponent } from './pages/home/home.component';
import { ProductsComponent } from './pages/products/products.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { CartComponent } from './pages/cart/cart.component';
import { AdminLoginComponent } from './pages/admin/admin-login/admin-login.component';
import { AdminDashboardComponent } from './pages/admin/admin-dashboard/admin-dashboard.component';
import { AdminProductsComponent } from './pages/admin/admin-products/admin-products.component';
import { AdminStatisticsComponent } from './pages/admin/admin-statistics/admin-statistics.component';
import { AdminUsersComponent } from './pages/admin/admin-users/admin-users.component';


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    CartDropdownComponent,
    HomeComponent,
    ProductsComponent,
    LoginComponent,
    RegisterComponent,
    CartComponent,
    AdminLoginComponent,
    AdminDashboardComponent,
    AdminProductsComponent,
    AdminStatisticsComponent,
    AdminUsersComponent
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

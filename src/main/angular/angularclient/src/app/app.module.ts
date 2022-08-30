import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { PostListComponent } from './_components/posts/post-list/post-list.component';
import { UserListComponent } from './_components/users/user-list/user-list.component';
import { LoginComponent } from './_components/login/login.component';
import { RegisterComponent } from './_components/register/register.component';
import { ChangePasswordComponent } from './_components/change-password/change-password.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { UserService } from './_services/user.service';
import { ForgotPassComponent } from './_components/forgot-pass/forgot-pass.component';
import { HomePageComponent } from './_components/home-page/home-page.component';
import { PageNotFoundComponent } from './_components/page-not-found/page-not-found.component';

@NgModule({
  declarations: [
    AppComponent,
    PostListComponent,
    UserListComponent,
    LoginComponent,
    RegisterComponent,
    ChangePasswordComponent,
    ForgotPassComponent,
    HomePageComponent,
    PageNotFoundComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [UserService],
  bootstrap: [AppComponent]
})
export class AppModule {

}

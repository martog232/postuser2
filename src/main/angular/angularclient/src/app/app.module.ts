import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { PostListComponent } from './_components/posts/post-list/post-list.component';
import { UserListComponent } from './_components/users/user-list/user-list.component';
import { LoginComponent } from './_components/login/login.component';
import { RegisterComponent } from './_components/register/register.component';
import { ChangePasswordComponent } from './_components/change-password/change-password.component';

@NgModule({
  declarations: [
    AppComponent,
    PostListComponent,
    UserListComponent,
    LoginComponent,
    RegisterComponent,
    ChangePasswordComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {

}

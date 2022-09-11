import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, Routes } from '@angular/router';
import { ChangePasswordComponent } from './_components/change-password/change-password.component';
import { CheckEmailComponent } from './_components/check-email/check-email.component';
import { ForgotPassComponent } from './_components/forgot-pass/forgot-pass.component';
import { GroupComponent } from './_components/group/group.component';
import { LoginComponent } from './_components/login/login.component';
import { PageNotFoundComponent } from './_components/page-not-found/page-not-found.component';
import { RegisterComponent } from './_components/register/register.component';

const routes: Routes = [
  { path: 'sign-in',title:'Sign in', component:LoginComponent},
  { path: 'sign-up',title:'Sign up', component:RegisterComponent},
  { path: 'forgot-pass',title:'Forgot password', component:ForgotPassComponent},
  { path: 'reset-pass/:token',title:'Change password', component:ChangePasswordComponent},
  { path: 'groups/:id', component:GroupComponent},
  { path: 'check-email',component:CheckEmailComponent},
  { path: '**',title:'Page Not Found', component:PageNotFoundComponent}
];

@NgModule({
  declarations: [],
  imports: [
  RouterModule.forRoot(routes),
  BrowserModule,
  HttpClientModule,
  FormsModule],
  exports: [RouterModule]
})
export class AppRoutingModule { }

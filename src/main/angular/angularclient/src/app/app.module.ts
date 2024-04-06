import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { SweetAlert2Module } from '@sweetalert2/ngx-sweetalert2';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './_components/login/login.component';
import { RegisterComponent } from './_components/register/register.component';
import { ChangePasswordComponent } from './_components/change-password/change-password.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserService } from './_services/user.service';
import { ForgotPassComponent } from './_components/forgot-pass/forgot-pass.component';
import { HomePageComponent } from './_components/home-page/home-page.component';
import { PageNotFoundComponent } from './_components/page-not-found/page-not-found.component';
import { GroupComponent } from './_components/group/group.component';
import { PostComponent } from './_components/post/post.component';
import { NavBarComponent } from './_components/nav-bar/nav-bar.component';
import { CommentComponent } from './_components/comment/comment.component';
import { GroupService } from './_services/group.service';
import { CookieService } from 'ngx-cookie-service';
import { CheckEmailComponent } from './_components/check-email/check-email.component';
import { CreatePostComponent } from './_components/create-post/create-post.component';
import { ImageComponent } from './_components/image/image.component';
import { AddCommentComponent } from './_components/add-comment/add-comment.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { EditPostComponent } from './_components/edit-post/edit-post.component';
import { AddAdminComponent } from './_components/add-admin/add-admin.component';
import { UserComponent } from './_components/user/user.component';
import { GroupListComponent } from './_components/group-list/group-list.component';
import { CreateGroupComponent } from './_components/create-group/create-group.component';
import { EditCommentComponent } from './_components/edit-comment/edit-comment.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    ChangePasswordComponent,
    ForgotPassComponent,
    HomePageComponent,
    PageNotFoundComponent,
    GroupComponent,
    PostComponent,
    NavBarComponent,
    CommentComponent,
    CheckEmailComponent,
    CreatePostComponent,
    ImageComponent,
    AddCommentComponent,
    EditPostComponent,
    AddAdminComponent,
    UserComponent,
    GroupListComponent,
    CreateGroupComponent,
    EditCommentComponent
  ],
  imports: [
    SweetAlert2Module.forRoot(),
    SweetAlert2Module,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    NgbModule
  ],
  providers: [
    UserService,
    GroupService,
    CookieService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {

}

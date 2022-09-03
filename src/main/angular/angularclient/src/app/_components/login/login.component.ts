import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserLoginDTO } from 'src/app/_models/user/login-user.model';
import { UserService } from 'src/app/_services/user.service';
import { Observable, Subscription } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  emptyLogin : UserLoginDTO = {
    username: '',
    password: ''
  }

  loginSubscription: Subscription | undefined;

  constructor(private userService: UserService,public router: Router, private route:ActivatedRoute) { }

  ngOnInit(): void {
    localStorage.setItem("key","marto")
  }

  ngOnDestroy(): void {
    this.loginSubscription?.unsubscribe();
  }

  onLogUser(): void {
    this.loginSubscription = this.userService.login(this.emptyLogin).subscribe(
      (response: string) => {
        console.log(response);
      }
    )
  }

}

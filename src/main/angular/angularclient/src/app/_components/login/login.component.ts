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


  constructor(private userService: UserService,public router: Router) { }

  ngOnInit(): void {
    localStorage.setItem("key","marto")
  }

        // localStorage.setItem('JSESSIONID', response);

  onLogUser(): void {
    this.userService.login(this.emptyLogin).subscribe(
      (response: any) => {
        console.log(response);
        this.router.navigate(['/groups/',"10"]);
      }
    )
  }

}

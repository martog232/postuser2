import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserLoginDTO } from 'src/app/_models/user/login-user.model';
import { UserService } from 'src/app/_services/user.service';
import swal from 'sweetalert2'

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  emptyLogin: UserLoginDTO = {
    username: '',
    password: ''
  }


  constructor(private userService: UserService, public router: Router) { }

  ngOnInit(): void {

  }

  checkFilledFields(): boolean {
    var result = false;
    if (this.emptyLogin.username
      && this.emptyLogin.password) result = true;
    return result;
  }

  //todo localStorage.setItem('JSESSIONID', response);

  onLogUser(): void {
    if (this.checkFilledFields()) {
      this.userService.login(this.emptyLogin).subscribe(
        (response: any) => {
          console.log(response);
          localStorage.setItem('logged user', response.username);
          localStorage.setItem('loggedId', response.id)
          this.router.navigate(['/users/', response.username]);
        }, (err) => {
          console.error(err)
        if(err = '401'){
          swal.fire({
            text: 'Email or pass not correct',
            icon: 'warning',
            iconColor: 'red',
            cancelButtonText: 'OK',
            cancelButtonColor: 'red'
          }
          )
        }}
      )
    } else {
      swal.fire({
        text: 'Please fill out all required fields.',
        icon: 'warning',
        iconColor: 'red',
        cancelButtonText: 'OK',
        cancelButtonColor: 'red'
      })
    }
  }

}

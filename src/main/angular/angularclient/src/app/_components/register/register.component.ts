import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RegisterRequestUserDTO } from 'src/app/_models/user/register-user.model';
import { UserService } from 'src/app/_services/user.service';
import { Observable, Subscription } from 'rxjs';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import swal from 'sweetalert2'

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  emptyRegistration: RegisterRequestUserDTO = {
    username: '',
    email: '',
    password: '',
    confirmPassword: ''
  }

  regForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('')
  })


  registerSubscription: Subscription | undefined;

  constructor(private userService: UserService, public router: Router) { }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
    this.registerSubscription?.unsubscribe();
  }

  checkFilledFields(): boolean {
    var result = false;
    if (this.emptyRegistration.username
      && this.emptyRegistration.email
      && this.emptyRegistration.password
      && this.emptyRegistration.confirmPassword) result = true;
    return result;
  }

  onRegUser(): void {
    var passedEmail = this.emptyRegistration.email;
    var pattern = new RegExp('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$');
    var result = pattern.test(passedEmail)
    if ((this.checkFilledFields())
      && this.emptyRegistration.password == this.emptyRegistration.confirmPassword
      && result) {

      this.userService.register(this.emptyRegistration).subscribe(
        (response: string) => {
          console.log(response);
          this.router.navigate(['/check-email']);
        },
        (err) => {
          console.error(err)
        if(err = '409'){
          swal.fire({
            text: 'Email or username already exists',
            icon: 'warning',
            iconColor: 'red',
            cancelButtonText: 'OK',
            cancelButtonColor: 'red'
          }
          )
        }}
      )
    }
    else {

      if (!this.checkFilledFields()) {
        swal.fire({
          text: 'Please fill out all required fields.',
          icon: 'warning',
          iconColor: 'red',
          cancelButtonText: 'OK',
          cancelButtonColor: 'red'
        }
        )
        return;
      }
      if (!result) {
        swal.fire({
          text: 'Email is not correct',
          icon: 'warning',
          iconColor: 'red',
          cancelButtonText: 'OK',
          cancelButtonColor: 'red'
        })
        return;
      }
      if (this.emptyRegistration.password != this.emptyRegistration.confirmPassword) {
        swal.fire({
          text: 'Passwords are not same',
          icon: 'warning',
          iconColor: 'red',
          cancelButtonText: 'OK',
          cancelButtonColor: 'red'
        })
        return;
      }
    }
  }
}

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RegisterRequestUserDTO } from 'src/app/_models/user/register-user.mode';
import { UserService } from 'src/app/_services/user.service';
import { Observable, Subscription } from 'rxjs';

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


  registerSubscription: Subscription | undefined;

  constructor(private userService: UserService,public router: Router) { }

  ngOnInit(): void {
  }

  ngOnDestroy() : void{
    this.registerSubscription?.unsubscribe();
  }

  onRegUser():void{
    this.userService.register(this.emptyRegistration).subscribe(
      (response : string) => {
        console.log(response);
        this.router.navigate(['/check-email']);
      }
    )
  }
}

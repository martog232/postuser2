import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserService } from 'src/app/_services/user.service';

@Component({
  selector: 'app-forgot-pass',
  templateUrl: './forgot-pass.component.html',
  styleUrls: ['./forgot-pass.component.css']
})
export class ForgotPassComponent implements OnInit {

  emptyEmail: string = '';

  sendEmailForgotPassSubscription: Subscription | undefined;

  constructor(private service: UserService, public router: Router) { }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
    this.sendEmailForgotPassSubscription?.unsubscribe();
  }

  onForgotPass(): void {
   this.service.forgotPass(this.emptyEmail).subscribe(
    (response: any) => {
      this.router.navigate(['/check-email']);
    }
   )
  }

}

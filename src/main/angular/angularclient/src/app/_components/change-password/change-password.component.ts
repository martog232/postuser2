import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ChangePassDTO } from 'src/app/_models/user/change-pass.model';
import { UserService } from 'src/app/_services/user.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {

  emptyChangePassDTO: ChangePassDTO = {
    newPass: '',
    confirmNewPass: ''
  }

  constructor(private userService: UserService, public router: Router) { }

  ngOnInit(): void {
    var btnVal = document.getElementById('changePassBtn');
    if (btnVal) btnVal.setAttribute('val', this.router.url.substring(12));
  }

  changePass(): void {
    var btnVal: string;
    var btn = document.getElementById('changePassBtn');
    if (btn) {
      btnVal = btn.getAttribute('val')!
      this.userService.changePass(this.emptyChangePassDTO, btnVal).subscribe(
        (response: any) => {
          if(response.status == 200) this.router.navigate(['/sign-in']);
          
        }
      )
    }
  } 

  // onLogUser(): void {
  //   this.userService.login(this.emptyLogin).subscribe(
  //     (response: any) => {
  //       console.log(response);
  //       this.router.navigate(['/groups/',"10"]);
  //     }
  //   )
  // }
}

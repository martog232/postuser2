import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/_services/user.service';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {

  constructor(private userService: UserService, public router: Router) { }

  ngOnInit(): void {
  }

  onSignOutUser(): void {
    localStorage.setItem('logged user',null);
    localStorage.setItem('loggedId',null);
  this.router.navigate(['/sign-in']);
      
    
  }

}

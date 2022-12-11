import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Post } from 'src/app/_models/post.model';
import { UserWithName } from 'src/app/_models/user/user-with-name.model';
import { User } from 'src/app/_models/user/user.model';
import { UserService } from 'src/app/_services/user.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  postsList: Post[] = [];
  usersList: UserWithName[] = [];

  loggedUsername: string = '';

  emptyUser: User = {
    id: undefined,
    username: '',
    posts: this.postsList,
    followers: this.usersList,
    followings: this.usersList
  }

  userSubscription: Subscription | undefined;

  constructor(private userService: UserService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.getUser(this.route.snapshot.paramMap.get('username'));
  }

  getUser(username: string | null): any {
    if (!username) {
      return;
    }
    this.userSubscription = this.userService.getUser(username).subscribe(
      data => {
        data.posts.forEach(post => {
          post.imageList = post.imageList.map(image => ({
            id: image.id,
            url: `${environment.apiBaseUrl}/images/${image.id}`
          }))
        })
        this.emptyUser = data;
        this.loggedUsername = localStorage.getItem('logged user')
      }
    );
  }

}

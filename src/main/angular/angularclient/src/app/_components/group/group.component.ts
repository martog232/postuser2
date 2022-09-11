import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Subscription, VirtualTimeScheduler } from 'rxjs';
import { Group } from 'src/app/_models/group.model';
import { Post } from 'src/app/_models/post.model';
import { UserWithName } from 'src/app/_models/user/user-with-name.model';
import { User } from 'src/app/_models/user/user.model';
import { GroupService } from 'src/app/_services/group.service';
import { PostService } from 'src/app/_services/post.service';
import { UserService } from 'src/app/_services/user.service';

@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.css']
})
export class GroupComponent implements OnInit {

  postsList: Post[] = [];
  usersList: UserWithName[] = [];

  emptyGroup: Group = {
    id: undefined,
    name: '',
    description: '',
    posts: this.postsList,
    members: this.usersList,
    admins: this.usersList
  }

  groupSubscription: Subscription | undefined;
  postsSubscription: Subscription | undefined;

  constructor(private groupService: GroupService, private postService: PostService, private userService: UserService, private router: Router, private route: ActivatedRoute,
   ) { }

  ngOnInit(): void {
    this.getGroup(this.route.snapshot.paramMap.get('id'));
  }

  getGroup(id: string | null): any {
    if (!id) {
      return;
    }
    this.groupSubscription = this.groupService.getGroup(+id).subscribe(
      data => {
        console.log(localStorage.getItem('JSESSIONID'));
        console.log(data);
        this.emptyGroup = data;
      }
    );
  }
  openDialogForCreatingPost(): void {
    console.log('mamam');
    
  }
}

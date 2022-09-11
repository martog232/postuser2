import { Component, OnInit } from '@angular/core';
import { UserWithName } from 'src/app/_models/user/user-with-name.model';
import { Comment } from 'src/app/_models/comment.model'

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent implements OnInit {

  likers: UserWithName[] = [];

  comment: Comment = {
    id: undefined,
    content: '',
    owner: {
      id: undefined,
      username: ''
    },
    likers: this.likers
  }

  constructor() { }

  ngOnInit(): void {
  }

}

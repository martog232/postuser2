import { Component, OnInit } from '@angular/core';
import { Image } from 'src/app/_models/image.model';
import { Post } from 'src/app/_models/post.model';
import { Comment } from 'src/app/_models/comment.model';
import { UserWithName } from 'src/app/_models/user/user-with-name.model';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {

  likers: UserWithName[] = [];
  comments: Comment[] = [];
  images: Image[] = [];

  emptyPost: Post = {
    id: undefined,
    content: '',
    imageList: this.images,
    owner: {
      id: undefined,
      username: ''
    },
    likers: this.likers,
    comments: this.comments,
  }

  constructor() { }

  ngOnInit(): void {
  }

}

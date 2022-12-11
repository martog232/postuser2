import { Component, Input, OnInit } from '@angular/core';
import { UserWithName } from 'src/app/_models/user/user-with-name.model';
import { Comment } from 'src/app/_models/comment.model'
import { PostService } from 'src/app/_services/post.service';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent implements OnInit {

  likers: UserWithName[] = [];

  @Input() comment: Comment;

  constructor(private readonly postService: PostService) { }

  ngOnInit(): void {
  }

  likeUnlikeComment(id:any){
    this.postService.likeUnlikeComment(id)
    .subscribe(
     (updatedComment: Comment) =>{
      this.comment = updatedComment;
     }
   )

 }

}

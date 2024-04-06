import { Component, Input, OnInit } from '@angular/core';
import { UserWithName } from 'src/app/_models/user/user-with-name.model';
import { Comment } from 'src/app/_models/comment.model'
import { CommentService } from 'src/app/_services/comment.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import swal from 'sweetalert2'
import { EditCommentComponent } from '../edit-comment/edit-comment.component';
import { PostService } from 'src/app/_services/post.service';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent implements OnInit {

  likers: UserWithName[] = [];

  @Input() comment: Comment;

  constructor(private readonly commentService: CommentService,private readonly postService:PostService, private readonly modalService: NgbModal) { }

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

//  deleteComment(id: any, ownerId: string) {
//   if (ownerId != localStorage.getItem('logged user')) {
//     swal.fire({
//       text: 'You cant delete other\'s comments',
//       icon: 'warning',
//       iconColor: 'red',
//       cancelButtonText: 'OK',
//       cancelButtonColor: 'red'
//     })
//   } else {
//     swal.fire({
//       title: 'Wait',
//       text: 'Are you sure you want to delete this comment',
//       icon: 'warning',
//       iconColor: 'red',
//       confirmButtonText: 'Yes',
//       cancelButtonText: 'No, keep it',
//     }).then((result) => {
//       if (result.value) {
//         this.commentService.deleteComment(id).subscribe(
//           window.location.reload)
//       }
//     })
//   }
// }

openEditCommentModal(ownerId: string): void {
  if (ownerId != localStorage.getItem('logged user')) {
    swal.fire({
      text: 'You cant edit other\'s comments',
      icon: 'warning',
      iconColor: 'red',
      cancelButtonText: 'OK',
      cancelButtonColor: 'red'
    })
  } else {
    const modalRef = this.modalService.open(EditCommentComponent, { centered: true });
    modalRef.componentInstance.commentId = this.comment.id;
    modalRef.componentInstance.ownerUsername = this.comment.owner.username;

    modalRef.result.then(() => {
    }).catch((error) => {
      modalRef.close();
    });
  }
}

}

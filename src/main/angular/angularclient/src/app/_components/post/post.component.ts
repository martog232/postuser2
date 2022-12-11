import { Component, Input, OnInit } from '@angular/core';
import { Image } from 'src/app/_models/image.model';
import { Post } from 'src/app/_models/post.model';
import { Comment } from 'src/app/_models/comment.model';
import { UserWithName } from 'src/app/_models/user/user-with-name.model';
import { PostService } from 'src/app/_services/post.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AddCommentComponent } from '../add-comment/add-comment.component';
import { environment } from 'src/environments/environment';
import { EditPostComponent } from '../edit-post/edit-post.component';
import swal from 'sweetalert2'

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {

  likers: UserWithName[] = [];
  comments: Comment[] = [];
  imageList: Image[] = [];

  emptyPost: Post = {
    id: undefined,
    content: '',
    imageList: this.imageList,
    owner: {
      id: undefined,
      username: ''
    },
    likers: this.likers,
    comments: this.comments,
    created: new Date
  }

  simpleComment: Comment = {
    id: undefined,
    content: '',
    owner: {
      id: undefined,
      username: ''
    },
    likers: this.likers
  }

  @Input() post: Post | undefined;

  constructor(private readonly postService: PostService, private readonly modalService: NgbModal) {
  }

  ngOnInit(): void {
  }


  likeUnlike(id: any) {
    this.postService.likeUnlike(id)
      .subscribe(
        (updatedPost: any) => {
          updatedPost.imageList = updatedPost.imageList.map(image => ({
            id: image.id,
            url: `${environment.apiBaseUrl}/images/${image.id}`
          }))
          this.post = updatedPost;
        }
      )
  }

  openAddCommentModal(): void {
    const modalRef = this.modalService.open(AddCommentComponent, { centered: true });
    modalRef.componentInstance.postId = this.post.id;
    modalRef.result.then(() => {
      window.location.reload();
    }).catch((error) => {
      console.log(error);
    });
  }

  deletePost(id: any,ownerId:string) {
    if(ownerId!=localStorage.getItem('logged user')){
      swal.fire({
        text:'You cant delete other\'s posts',
        icon: 'warning',
        iconColor:'red',
        cancelButtonText:'OK',
        cancelButtonColor:'red'
      })
    }else 
    swal.fire({
      title: 'Wait',
      text:'Are you sure you want to delete this post',
      icon: 'warning',
      iconColor:'red',
      confirmButtonText: 'Yes',
      cancelButtonText:'No, keep it',
    }).then((result)=>{
      if (result.value) {
      this.postService.deletePost(id).subscribe(
    window.location.reload)}
    })
   }

  openEditPostModal(ownerId:string): void {
    if(ownerId!=localStorage.getItem('logged user')){
      swal.fire({
        text:'You cant edit other\'s posts',
        icon: 'warning',
        iconColor:'red',
        cancelButtonText:'OK',
        cancelButtonColor:'red'
      })
    }else{
    const modalRef = this.modalService.open(EditPostComponent, { centered: true });
    modalRef.componentInstance.postId = this.post.id;
    modalRef.componentInstance.ownerUsername = this.post.owner.username;

    modalRef.result.then(() => {
    }).catch((error) => {
      modalRef.close();
    });
  }
  }
}

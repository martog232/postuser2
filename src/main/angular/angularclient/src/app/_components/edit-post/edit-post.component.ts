import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { PostService } from 'src/app/_services/post.service';

@Component({
  selector: 'app-edit-post',
  templateUrl: './edit-post.component.html',
  styleUrls: ['./edit-post.component.css']
})
export class EditPostComponent implements OnInit {


  @Input() public postId:number |undefined;
  @Input() public ownerUsername:string |undefined;

  content:string ='';

  constructor(public activeModal: NgbActiveModal,private postService :PostService) { }

  ngOnInit(): void {
  }

  closeModal() {
    this.activeModal.close();
  }

  onEditPost(){
    if(this.ownerUsername!=localStorage.getItem('logged user')){
      alert('you cant delete others posts')
      this.closeModal
    }else
    this.postService.editPost(this.content,this.postId).subscribe(
      res => console.log(res))
  }

}

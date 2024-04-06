import { Input } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { GroupService } from 'src/app/_services/group.service';
import { PostService } from 'src/app/_services/post.service';

@Component({
  selector: 'app-add-comment',
  templateUrl: './add-comment.component.html',
  styleUrls: ['./add-comment.component.css']
})
export class AddCommentComponent implements OnInit {

  content: string = '';

  @Input() public postId: number | undefined
  @Input() public groupId: number | undefined

  constructor(private activeModal: NgbActiveModal,private postService: PostService) { }

  ngOnInit(): void {
  }

  closeModal() {
    this.activeModal.close('Modal Closed');
  }

  onAddComment() {
    this.postService.addComment(this.content, this.postId).subscribe(
      (response) => {
        this.activeModal.close(this.groupId)
      }
    )
  }


}

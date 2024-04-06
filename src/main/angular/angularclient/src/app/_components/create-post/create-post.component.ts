import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { GroupService } from 'src/app/_services/group.service';
import { PostService } from 'src/app/_services/post.service';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.css']
})
export class CreatePostComponent implements OnInit {
  content: string = '';
  file: FileList | null;

  @Input() public groupId: number | undefined

  constructor(public activeModal: NgbActiveModal, private postService: PostService) { }

  ngOnInit(): void {
  }

  closeModal() {
    this.activeModal.close('Modal Closed');
  }

  onCreatePost() {
    var multipartfile = (this.file && this.file.length)
      ? this.file[0]
      : null;
    this.postService.createPost(this.content, multipartfile, this.groupId).subscribe(
      (response) => {
        this.activeModal.close(this.groupId);
      })

  }

}

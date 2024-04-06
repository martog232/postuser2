import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { GroupService } from 'src/app/_services/group.service';
import { PostService } from 'src/app/_services/post.service';

@Component({
  selector: 'app-add-admin',
  templateUrl: './add-admin.component.html',
  styleUrls: ['./add-admin.component.css']
})
export class AddAdminComponent implements OnInit {

  
  username: string = '';

  @Input() public groupId: number | undefined
  
  constructor(public activeModal: NgbActiveModal, private postService: PostService, private groupService: GroupService) { }

  ngOnInit(): void {
  }

  closeModal() {
    this.activeModal.close('Modal Closed');
  }

  onCreatePost() {
    this.groupService.addAdmin(this.username, this.groupId).subscribe(
      (response) => {
        this.activeModal.close(this.groupId);
      })

  }

}

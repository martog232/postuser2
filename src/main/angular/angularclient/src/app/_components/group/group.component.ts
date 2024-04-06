import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Group } from 'src/app/_models/group.model';
import { Post } from 'src/app/_models/post.model';
import { UserWithName } from 'src/app/_models/user/user-with-name.model';
import { GroupService } from 'src/app/_services/group.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CreatePostComponent } from '../create-post/create-post.component';
import { environment } from 'src/environments/environment';
import { AddAdminComponent } from '../add-admin/add-admin.component';
import swal from 'sweetalert2';
import { Title } from "@angular/platform-browser";

@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.css']
})
export class GroupComponent implements OnInit {

  postsList: Post[] = [];
  usersList: UserWithName[] = [];

  loggedUsername: string = '';

  emptyGroup: Group = {
    id: undefined,
    name: '',
    description: '',
    posts: this.postsList,
    members: this.usersList,
    admins: this.usersList
  }

  groupSubscription: Subscription | undefined;

  constructor(private groupService: GroupService, private route: ActivatedRoute, private modalService: NgbModal,
    private titleService: Title) { }

  ngOnInit(): void {
    this.getGroup(this.route.snapshot.paramMap.get('id'));
  }

  getGroup(id: string | null): any {
    if (!id) {
      return;
    }
    this.groupSubscription = this.groupService.getGroup(+id).subscribe(
      data => {
        data.posts.forEach(post => {
          post.imageList = post.imageList.map(image => ({
            id: image.id,
            url: `${environment.apiBaseUrl}/images/${image.id}`
          }))
        })
        this.emptyGroup = data;
        this.loggedUsername = localStorage.getItem('logged user');
        this.titleService.setTitle(this.emptyGroup.name);
      }
    );
  }

  onJoinLeave(id: number) {
    this.groupService.joinLeave(id)
      .subscribe(
        (response: Group) => {
          this.emptyGroup = response;
          window.location.reload();
        }
      )
  }

  openCreatePostModal(): void {
    const modalRef = this.modalService.open(CreatePostComponent);
    modalRef.componentInstance.groupId = this.emptyGroup.id;
    modalRef.result.then((groupId) => {
      this.getGroup(groupId);
    }).catch((error) => {
      console.log(error);
    });
  }

  openAddAdminModal(): void {
    const modalRef = this.modalService.open(AddAdminComponent);
    modalRef.componentInstance.groupId = this.emptyGroup.id;
    modalRef.result.then((groupId) => {
      this.getGroup(groupId);
    }).catch((error) => {
      console.log(error);
    });
  }

  addMember(userList: any) {
    var isAdmin = false;
    for (let admin of userList) {
      if (admin.username == localStorage.getItem('logged user')) { isAdmin = true }
    }
    if (!isAdmin) {
      swal.fire({
        text:'You are not Admin to do that',
        icon: 'warning',
        iconColor:'red',
        cancelButtonText:'OK',
        cancelButtonColor:'red'
      })
    }
    else {
    swal.fire({
      text:'Waiting fo an update',
      icon: 'info',
      iconColor:'red',
      cancelButtonText:'OK',
      cancelButtonColor:'red'
    })}
  }

  addAdmin(userList: any) {
    var isAdmin = false;
    for (let admin of userList) {
      if (admin.username == localStorage.getItem('logged user')) { isAdmin = true }
    }
    if (!isAdmin) {
      swal.fire({
        text:'You are not Admin to do that',
        icon: 'warning',
        iconColor:'red',
        cancelButtonText:'OK',
        cancelButtonColor:'red'
      })
    }
    else this.openAddAdminModal();
  }
}

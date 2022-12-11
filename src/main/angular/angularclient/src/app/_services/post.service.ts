import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators'
import { environment } from 'src/environments/environment';
import { Post } from '../_models/post.model'
import { UserWithName } from '../_models/user/user-with-name.model';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public getListOfPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(this.apiServerUrl + '/posts');
  }

  public getPost(id: number): Observable<Post> {
    return this.http.get<Post>(this.apiServerUrl + '/posts/' + id)
      .pipe(catchError(this.errorhandler));
  }

  public likeUnlike(postId: number) {
    const formData = new FormData();
    formData.append('loggedUserId', localStorage.getItem('loggedId'));
    return this.http.post<Post>(this.apiServerUrl + '/posts/' + postId + '/like', formData);

  }

  public likeUnlikeComment(id: number): Observable<any> { 
    const formData = new FormData();
    formData.append('loggedUserId', localStorage.getItem('loggedId'));
    return this.http.post<Comment>(this.apiServerUrl + 'comments/' + id + '/like', formData);

  }

  public createPost(content: string,file:File, id: number) {
    const HttpUploadOptions = {
      headers: new HttpHeaders({ "Content-Type": "multipart/form-data"})
    }
    const formData = new FormData();
    formData.append('content', content); 
    formData.append('photoList', file);
    formData.append('groupId', id.toString());
    formData.append('loggedUserId', localStorage.getItem('loggedId'));
    return this.http.post<Post>(this.apiServerUrl + '/posts', formData);
  }

  public editPost(content:string,id:number){
    const formData = new FormData();
    formData.append('content',content);
    formData.append('loggedUserId', localStorage.getItem('loggedId'));
    return this.http.post<Post>(this.apiServerUrl + 'posts/' + id + '/edit', formData)
  }

  public addComment(content: string, id: number) {
    const formData = new FormData();
    formData.append('content', content);
    formData.append('loggedUserId', localStorage.getItem('loggedId'));
    return this.http.post<Comment>(this.apiServerUrl + 'comments/' + id, formData);
  }

  public deletePost(id:number){
    return this.http.delete(this.apiServerUrl + 'posts/' + id +'?loggedUserId=' + localStorage.getItem('loggedId'));
  }

  errorhandler(error: HttpErrorResponse) {
    return throwError(error);
  }
}

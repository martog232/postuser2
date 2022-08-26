import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators'
import { environment } from 'src/environments/environment';
import { Post } from '../_models/post.model'

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public getListOfCountries(): Observable<Post[]> {
    return this.http.get<Post[]>(this.apiServerUrl + '/posts');
  }

  public getPost(id: number): Observable<Post> {
    return this.http.get<Post>(this.apiServerUrl + '/posts/' + id)
                    .pipe(catchError(this.errorhandler));
  }

  errorhandler(error: HttpErrorResponse) {
    return throwError(error);
  }
}

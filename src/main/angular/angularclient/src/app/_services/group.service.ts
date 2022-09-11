import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { ErrorHandler, Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { catchError, map, Observable, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Group } from '../_models/group.model';

@Injectable({
  providedIn: 'root'
})
export class GroupService {


  private apiServerUrl = environment.apiBaseUrl;
  params = new HttpParams();
  constructor(private http: HttpClient, private cookieService: CookieService) {
    const cookieValue = this.cookieService.get('JSESSIONID');
  }

  public getGroup(id: number): Observable<Group> {
    return this.http.get<Group>(this.apiServerUrl + 'groups/' + id)
      .pipe(map(response => ({
        name: response.name,
        description: response.description,
        posts: response.posts,
        members: response.members,
        admins: response.admins
      })));
  }

  public getGroupList(name: string): Observable<Group[]> {
    return this.http.get<Group[]>(this.apiServerUrl + 'groups/search/');
  }

  errorhandler(error: HttpErrorResponse) {
    return throwError(error);
  }
}

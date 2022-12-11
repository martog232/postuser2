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
    const formData = new FormData();
    formData.append('loggedUserId', localStorage.getItem('loggedId'));
    return this.http.get<Group>(this.apiServerUrl + 'groups/' + id +'?loggedUserId=' + localStorage.getItem('loggedId'));
  }

  public getGroupList(name: string): Observable<Group[]> {
    return this.http.get<Group[]>(this.apiServerUrl + 'groups/search/');
  }

  public joinLeave(id:number){
    const formData = new FormData();
    formData.append('loggedUserId', localStorage.getItem('loggedId'));
    return this.http.post<Group>(this.apiServerUrl + 'groups/' + id + '/join',formData);
  }

  public addAdmin(newAdminUserName:string,groupId:number){
    const formData = new FormData();
    formData.append('newAdminUserName', newAdminUserName);
    formData.append('loggedUserId', localStorage.getItem('loggedId'));
    return this.http.post<Group>(this.apiServerUrl + 'groups/' + groupId + '/add-admin',formData);
  }

  errorhandler(error: HttpErrorResponse) {
    return throwError(error);
  }
}

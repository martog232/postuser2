import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators'
import { environment } from 'src/environments/environment';
// @ts-ignore
import { User } from '../_models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public getListOfCountries(): Observable<User[]> {
    return this.http.get<User[]>(this.apiServerUrl + '/users');
  }

  public getUser(id: number): Observable<User> {
    return this.http.get<User>(this.apiServerUrl + '/users/' + id)
                    .pipe(catchError(this.errorhandler));
  }

  errorhandler(error: HttpErrorResponse) {
    return throwError(error);
  }
}

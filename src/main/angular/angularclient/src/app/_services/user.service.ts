import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators'
import { environment } from 'src/environments/environment';
// @ts-ignore
import { User } from '../_models/user.model';
import { UserLoginDTO } from '../_models/user/login-user.model';
import { RegisterRequestUserDTO } from '../_models/user/register-user.mode';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiServerUrl = environment.apiBaseUrl;
  private loginUrl = this.apiServerUrl + '/sign-in';
  private registerUrl = this.apiServerUrl + '/sign-up';
  private forgotPassUrl = this.apiServerUrl + '/forgot-pass'


  constructor(private http: HttpClient) { }

  public getListOfUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiServerUrl + '/users');
  }

  public getUser(id: number): Observable<User> {
    return this.http.get<User>(this.apiServerUrl + '/users/' + id)
                    .pipe(catchError(this.errorhandler));
  }

  public login(loginDTO:UserLoginDTO){
    return this.http.post<string>(`${this.loginUrl}`,loginDTO)
  }

  public register(registerDTO:RegisterRequestUserDTO){
    return this.http.post<string>(`${this.registerUrl}`,registerDTO)
  }

  public forgotPass(email :string){
    return this.http.post(`${this.forgotPassUrl}`,email)
  }

  errorhandler(error: HttpErrorResponse) {
    return throwError(error);
  }
}

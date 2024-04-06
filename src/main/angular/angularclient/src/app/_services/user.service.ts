import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators'
import { environment } from 'src/environments/environment';
// @ts-ignore
import { User } from '../_models/user.model';
import { UserLoginDTO } from '../_models/user/login-user.model';
import { RegisterRequestUserDTO } from '../_models/user/register-user.model';
import { ChangePassDTO } from '../_models/user/change-pass.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiServerUrl = environment.apiBaseUrl;
  private loginUrl = this.apiServerUrl + '/sign-in';
  private registerUrl = this.apiServerUrl + '/sign-up';
  private logoutUrl = this.apiServerUrl + '/sign-out';
  private forgotPassUrl = this.apiServerUrl + '/forgot-pass'


  constructor(private http: HttpClient) { }

  public getListOfUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiServerUrl + '/users');
  }

  public getUser(username:string): Observable<User> {
    return this.http.get<User>(this.apiServerUrl + 'users/' + username);
  }

  public login(loginDTO: UserLoginDTO) {
    return this.http.post<string>(`${this.loginUrl}`, loginDTO)
  }

  public logout() {
    return this.http.get(this.logoutUrl);
  }

  public register(registerDTO: RegisterRequestUserDTO) {
    return this.http.post<string>(`${this.registerUrl}`, registerDTO)
  }

  public forgotPass(email: string) {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("email",email);
    return this.http.get(`${this.forgotPassUrl}`,{params:queryParams})
  }

  public changePass(dto:ChangePassDTO,token:string) {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("token",token);
    return this.http.post<string>(this.apiServerUrl + '/reset-pass', dto,{params:queryParams})
  }

  errorhandler(error: HttpErrorResponse) {
    return throwError(error);
  }
}

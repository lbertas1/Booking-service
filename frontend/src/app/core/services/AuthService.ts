import {Injectable} from "@angular/core";
import {environment} from "../../../environments/environment";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {IUser} from "../interfaces";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  // public url = environment.apiBaseUrl;
  // private token: string;
  // private loggedInUsername: string;
  //
  // constructor(private http: HttpClient) {
  // }
  //
  // public login(user: IUser): Observable<HttpResponse<IUser>> {
  //   return this.http.post<IUser>(`${this.url}/user/login`, user, {observe: 'response'});
  // }
  //
  // public register(user: IUser): Observable<IUser> {
  //   return this.http.post<IUser>(`${this.url}/user/register`, user);
  // }
  //
  // public logOut(): void {
  //   this.token = null;
  //   this.loggedInUsername = null;
  //   localStorage.removeItem('user');
  //   localStorage.removeItem('token');
  //   localStorage.removeItem('users');
  // }
  //
  // public saveToken(token: string): void {
  //   this.token = token;
  //   localStorage.setItem('token', token);
  // }
  //
  // public addUserToLocalCache(user: IUser): void {
  //   localStorage.setItem('user', JSON.stringify(user));
  // }
  //
  // public getUserFromLocalCache(): IUser {
  //   return JSON.parse(localStorage.getItem('user'));
  // }
  //
  // public loadToken(): void {
  //   this.token = localStorage.getItem('token');
  // }
  //
  // public getToken(): string {
  //   return this.token;
  // }
}

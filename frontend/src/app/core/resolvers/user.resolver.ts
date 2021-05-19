import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from "@angular/router";
import {IUser, IUserProfile, IUserRequest} from "../interfaces";
import { Observable, throwError } from "rxjs";
import { JwtService, UserService } from "../services";
import { catchError } from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class UserResolver implements Resolve<IUserProfile>{

  constructor(
    private readonly _userService: UserService,
    private readonly _router: Router,
    private readonly _jwtService: JwtService
  ) {
  }

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<IUserProfile> | Promise<IUserProfile> | IUserProfile {
    return this._userService.profile(this._jwtService.getIdentity().id).pipe(
      catchError((err) => {
        this._router.navigateByUrl('/');
        return throwError(err)
      })
    )
  }
}

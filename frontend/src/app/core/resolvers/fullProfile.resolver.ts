import {Injectable} from "@angular/core";
import {IUserProfile} from "../interfaces";
import {ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot} from "@angular/router";
import {JwtService, ReservationService, UserService} from "../services";
import {Observable, throwError} from "rxjs";
import {catchError} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class FullProfileResolver implements Resolve<IUserProfile>{

  constructor(
    private readonly _userService: UserService,
    private readonly _router: Router,
    private readonly _jwtService: JwtService
  ) {
  }

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot):
    Observable<IUserProfile> |
    Promise<IUserProfile> |
    IUserProfile {
    return this._userService
      .profile(this._jwtService.getIdentity().id)
      .pipe(catchError((err) => {
            this._router.navigateByUrl('/');
            return throwError(err)
    }));
  }

}

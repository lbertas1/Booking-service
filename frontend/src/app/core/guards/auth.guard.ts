import {Injectable} from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  CanActivateChild,
  Router,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';
import {Observable} from 'rxjs';
import {JwtService} from "../services";
import {take, tap} from "rxjs/operators";
import {Role} from "../enums";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanActivateChild {

  constructor(private readonly _jwtService: JwtService,
              private readonly _router: Router) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.canActivateAnything();
  }

  canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.canActivateAnything();
  }

  private canActivateAnything(): Observable<boolean> {
    return this._jwtService.isAuthenticated$.pipe(
      take(1),
      tap(isAuth => {
        if (!isAuth) {
          this._router.navigateByUrl('/main');
        }
      })
    );
  }
}

import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from "@angular/router";
import { IRoom } from "../interfaces";
import { Observable, throwError } from "rxjs";
import { RoomsService } from "../services";
import { catchError } from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class RoomResolver implements Resolve<IRoom> {

  constructor(
    private readonly _roomService: RoomsService,
    private readonly _router: Router
  ) {
  }

  public resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot,
  ): Observable<IRoom> | Promise<IRoom> | IRoom | Observable<any> {
    return this._roomService.get(route.params.id).pipe(
      catchError((err) => {
        this._router.navigateByUrl('/');
        return throwError(err);
        // return throwError(err.message);
      })
    );
  }
}

import { Injectable } from "@angular/core";
import { NotifyFactory } from "../factories";
import { ToastrService } from "ngx-toastr";
import { INotify } from "../interfaces";
import { Subject } from "rxjs";
import { NotificationTypes } from "../enums";

@Injectable({
  providedIn: 'root'
})
export class NotifyService {

  private readonly _notifyStream$ = new Subject<INotify>();

  constructor(
    private readonly _notifyFactor: NotifyFactory,
    private readonly _toastrService: ToastrService
  ) {
    this._notifyStream$.subscribe(notify => {
      const { type, title, body } = notify;

      switch (type) {
        case NotificationTypes.SUCCESS :
          this._toastrService.success(body, title);
          break;

        case NotificationTypes.INFO :
          this._toastrService.info(body, title);
          break;

        case NotificationTypes.WARNING :
          this._toastrService.warning(body, title);
          break;

        case NotificationTypes.ERROR :
          this._toastrService.error(body, title);
          break;
      }
    });
  }

  public pushSuccess(title: string, body: string): void {
    this.propagateNewNotify(this._notifyFactor.createNotify(NotificationTypes.SUCCESS, title, body));
  }

  public pushInfo(title: string, body: string): void {
    this.propagateNewNotify(this._notifyFactor.createNotify(NotificationTypes.INFO, title, body));
  }

  public pushWarning(title: string, body: string): void {
    this.propagateNewNotify(this._notifyFactor.createNotify(NotificationTypes.WARNING, title, body));
  }

  public pushError(title: string, body: string): void {
    this.propagateNewNotify(this._notifyFactor.createNotify(NotificationTypes.ERROR, title, body));
  }

  private propagateNewNotify(notify: INotify): void {
    this._notifyStream$.next(notify);
  }
}

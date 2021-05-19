import { Injectable } from "@angular/core";
import { NotificationTypes } from "../enums";
import { INotify } from "../interfaces";

@Injectable({
  providedIn: 'root'
})
export class NotifyFactory {
  public createNotify(type: NotificationTypes, title: string, body: string): INotify {
    return {
      type,
      body,
      title
    }
  }
}

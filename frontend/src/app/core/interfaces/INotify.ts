import { NotificationTypes } from "../enums";


export interface INotify {
  type: NotificationTypes;
  title: string;
  body: string;
}

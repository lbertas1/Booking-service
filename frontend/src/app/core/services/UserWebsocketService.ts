import {Injectable} from "@angular/core";
import {WebSocketService1} from "./WebSocketService1";

@Injectable({
  providedIn: 'root'
})
export class UserWebsocketService extends WebSocketService1 {
  protected hub = '';
}

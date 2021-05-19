import {Injectable} from "@angular/core";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export abstract class WebSocketService1 {

  protected hub = '';

  private _socket: WebSocket;

  // public connect(cb: () => void): void {
  //  this._socket = new WebSocket(environment.wsUrl + this.hub);
  //  this._socket.addEventListener('open', cb);
  // }
  //
  // public createMethod<T>(cb: (payload: MessageEvent<T>) => void): void {
  //   this._socket.addEventListener('message', cb);
  // }
  //
  // public disconnect(): void {
  //   this._socket.close();
  // }
}

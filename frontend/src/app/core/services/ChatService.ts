import {Injectable} from "@angular/core";
import {ApiService} from "./ApiService";
import {ISocketChannelVariables} from "../interfaces";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private path: string = '/chat';

  constructor(
    private readonly _apiService: ApiService
  ) {
  }

  public startChat(socketChannel: ISocketChannelVariables): Observable<ISocketChannelVariables> {
    return this._apiService.post(`${ this.path }/open-socket-channel`, socketChannel);
  }

  public endChat(identifier: string): Observable<ISocketChannelVariables> {
    return this._apiService.post(`${ this.path }/close-socket-channel/${identifier}`);
  }
}

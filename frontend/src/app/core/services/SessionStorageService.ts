import {Injectable} from "@angular/core";
import {Guid} from "guid-typescript";

@Injectable({
  providedIn: 'root'
})
export class SessionStorageService {

  public createSession(): string {
    const guid = Guid.create();
    sessionStorage.setItem('session', guid.toString());
    return guid.toString();
  }

  public getSession(): string {
    return sessionStorage.getItem('session');
  }

  public setItem(key: string, value: string) {
    sessionStorage.setItem(key, value);
  }

  public getItem(key: string): string {
    return sessionStorage.getItem(key);
  }
}

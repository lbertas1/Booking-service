import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  constructor(
    private _httpClient: HttpClient
  ) { }

  public get(path: string, httpParams: HttpParams = new HttpParams()): Observable<any> {
    return this._httpClient.get(`${ environment.apiBaseUrl }${ path }`, { params: httpParams });
  }

  public post(path: string, body: {} = {}): Observable<any> {
    return this._httpClient.post(`${ environment.apiBaseUrl }${ path }`, JSON.stringify(body), { headers: { 'Content-Type': 'application/json' } });
  }

  public put(path: string, body: {} = {}): Observable<any> {
    return this._httpClient.put(`${ environment.apiBaseUrl }${ path }`, JSON.stringify(body), { headers: { 'Content-Type': 'application/json' } });
  }

  public delete(path: string): Observable<any> {
    return this._httpClient.delete(`${ environment.apiBaseUrl }${ path }`);
  }
}

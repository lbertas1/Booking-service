import {Injectable} from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import {Observable} from 'rxjs';
import { JwtService } from "../services";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(
    private readonly _jwtService: JwtService
  ) {
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = this._jwtService.getAccessToken();
    const headersConfig = {};
    if (token) {
      headersConfig['Authorization'] = 'Bearer ' + token;
    }

    headersConfig['Accept'] = 'application/json';

    const req = request.clone({ setHeaders: headersConfig });
    return next.handle(req);
  }
}

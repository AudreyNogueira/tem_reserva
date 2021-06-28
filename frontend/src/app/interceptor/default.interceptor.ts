import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpHeaders
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { SessionService } from '../shared/services/session.service';
import { catchError, tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { RoutesEnum } from '../models/routes.enum';

@Injectable()
export class DefaultInterceptor implements HttpInterceptor {

  constructor(
    private readonly sessionService: SessionService,
    private readonly router: Router,
  ) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.ignoreRoutes(request.url)) {

      const addHeaders = request.headers.append('Authorization', `Bearer ${this.sessionService.getAuthenticated()}`);
      const authReq = request.clone({ headers: addHeaders });

      return next.handle(authReq).pipe(catchError(error => {
        if (error.status === 401) {
          this.sessionService.logout();
          this.router.navigate([RoutesEnum.ABOUT]);
        }
        return throwError(error);
      }));
    }
    return next.handle(request);
  }

  ignoreRoutes(url: string): boolean {
    return url.includes('/login') ||
      url.includes('/user/create') ||
      url.includes('/restaurant/create') ||
      url.includes('https://viacep.com.br');
  }

}

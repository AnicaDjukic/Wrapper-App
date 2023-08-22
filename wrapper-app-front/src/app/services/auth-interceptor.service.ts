import { Injectable } from '@angular/core';
import { StorageService } from './storage.service';
import { HttpEvent, HttpHandler, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, mergeMap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService {

  constructor(
    private storageService: StorageService,
    private authService: AuthenticationService,
    private router: Router
  ) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.storageService.getToken();
    console.log('Token:', token);

    if (req.headers.get('anonymous')) {
      return next.handle(req);
    }

    let clonedReq = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });

    return next.handle(clonedReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          return this.handleUnauthorizedError(error, req, next);
        }
        return throwError(() => error);
      })
    );
  }

  private handleUnauthorizedError(
    error: HttpErrorResponse,
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (error.status === 401) {
      if (error.url?.endsWith('/refresh-token')) {
        // Prevent infinite loop when refresh token request fails
        this.storageService.clearToken();
        this.router.navigateByUrl('/');
        return throwError(() => error);
      }
  
      return this.authService.refreshToken(this.storageService.getRefreshToken()).pipe(
        mergeMap((data: { token: string; refreshToken: string; }) => {
          if (data.token) {
            this.storageService.storeTokenData(data.token, data.refreshToken);
            let clonedReqWithNewToken = req.clone({
              setHeaders: { Authorization: `Bearer ${data.token}` }
            });
            return next.handle(clonedReqWithNewToken);
          } else {
            this.storageService.clearToken();
            this.router.navigateByUrl('/');
            return throwError(() => new Error('Token refresh failed, please log in again.'));
          }
        }),
        catchError((refreshError) => {
          this.storageService.clearToken();
          this.router.navigateByUrl('/');
          return throwError(() => refreshError);
        })
      );
    }
  
    // If it's not a 401 error, just return the original error observable
    return throwError(() => error);
  }  
}

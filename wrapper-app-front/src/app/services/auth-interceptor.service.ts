import { Injectable } from '@angular/core';
import { StorageService } from './storage.service';
import { HttpEvent, HttpHandler, HttpRequest } from '@angular/common/http';
import { Observable, mergeMap } from 'rxjs';
import { Router } from '@angular/router';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService {

  constructor(private storageService: StorageService,
    private authService: AuthenticationService,
    private router: Router) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.storageService.getToken();
    const expDate = new Date(this.storageService.getExpirationDateFromToken(token) * 1000);

    if (req.headers.get('anonymous')) {
      return next.handle(req);
    }

    if (token) {
      if (expDate < new Date()) {
        return this.authService.refreshToken(this.storageService.getRefreshToken()).pipe(
          mergeMap((data: { token: string; refreshToken: string; }) => {
            if (data.token) {
              this.storageService.storeTokenData(data.token, data.refreshToken);
              req = req.clone({
                setHeaders: { Authorization: `Bearer ${data.token}` }
              });
            } else {
              this.storageService.clearToken();
              this.router.navigateByUrl('/');
            }
            return next.handle(req);
          })
        );
      } else {
        req = req.clone({
          setHeaders: { Authorization: `Bearer ${token}` }
        });
      }
    }

    return next.handle(req);
  }
}

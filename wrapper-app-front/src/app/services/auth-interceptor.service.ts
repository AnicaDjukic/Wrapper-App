import { Injectable } from '@angular/core';
import { StorageService } from './storage.service';
import { HttpEvent, HttpHandler, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService {

  constructor(private storageService: StorageService, private router: Router) { }
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.storageService.getToken()
    const expDate = new Date(this.storageService.getExpirationDateFromToken() * 1000)
    if(req.headers.get('anonymous')){
      return next.handle(req)
    }
    if (token) {
      if (expDate < new Date()) {
        this.router.navigateByUrl('/');
      }
      else {
        req = req.clone({
          setHeaders: { Authorization: `Bearer ${token}` }
        });
      }
    }
    return next.handle(req)
  }
}

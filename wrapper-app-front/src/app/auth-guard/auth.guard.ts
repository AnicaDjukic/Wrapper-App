import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { StorageService } from '../services/storage.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private storageService: StorageService, private router: Router) { }

  canActivate(
    _route: ActivatedRouteSnapshot,
    _state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const token = this.storageService.getToken();
    const refreshToken = this.storageService.getRefreshToken();

    if (token && refreshToken) {
      const tokenExpDate = this.storageService.getExpirationDateFromToken(token);
      const refreshTokenExpDate = this.storageService.getExpirationDateFromToken(refreshToken);

      if (tokenExpDate > new Date() || refreshTokenExpDate > new Date()) {
        return true;
      }
    }

    this.storageService.clearToken();
    this.router.navigate(['/']);
    return false;
  }
}

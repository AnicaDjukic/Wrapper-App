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
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const expDate = new Date(this.storageService.getExpirationDateFromToken(this.storageService.getToken()) * 1000);
    const expDateRefresh = new Date(this.storageService.getExpirationDateFromToken(this.storageService.getRefreshToken()) * 1000);
    if (!!this.storageService.getToken() && expDate > new Date() &&
      !!this.storageService.getRefreshToken() && expDateRefresh > new Date()) {
      return true;
    }
    this.storageService.clearToken();
    this.router.navigate(['']);
    return false;
  }

}

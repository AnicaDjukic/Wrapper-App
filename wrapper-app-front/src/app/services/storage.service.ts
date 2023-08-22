import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  constructor() { }

  clearToken() {
    window.sessionStorage.setItem('jwt', "");
    window.sessionStorage.setItem('refreshToken', "");
  }

  storeTokenData(token: string, refreshToken: string): void {
    sessionStorage.setItem('jwt', token);
    sessionStorage.setItem('refreshToken', refreshToken);
  }

  getToken() : any{
    return sessionStorage.getItem('jwt');
  }

  getRefreshToken() : any {
    return sessionStorage.getItem('refreshToken');
  }

  getExpirationDateFromToken(token: string): any {
    const tokenData = JSON.parse(window.atob(token.split('.')[1]));
    return new Date(tokenData.exp * 1000);
  }
}

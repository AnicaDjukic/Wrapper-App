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
    if (token) {
      const tokenSplit = token.split('.')
      const decoded = decodeURIComponent(encodeURIComponent(window.atob(tokenSplit[1])))
      const obj = JSON.parse(decoded)
      return obj.exp
    }
    return ""
  }
}

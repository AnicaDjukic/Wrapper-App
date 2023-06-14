import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginDto } from '../dtos/LoginDto';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private authUrl = "http://localhost:8080/auth"

  constructor(private http: HttpClient) { }

  login(loginDto: LoginDto) {
    return this.http.post(`${this.authUrl}/login`, loginDto);
  }

  refreshToken(refreshToken: any) : any {
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${refreshToken}`)
      .set('Anonymous', 'true')
    return this.http.get(`${this.authUrl}/refresh`, { 'headers': headers });
  }
}

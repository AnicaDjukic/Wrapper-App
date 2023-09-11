import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginDto } from '../dtos/LoginDto';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private authUrl = "/auth";

  constructor(private http: HttpClient, private configService: ConfigService) {}
  
  login(loginDto: LoginDto) {
    let url = '';
    url = this.configService.getBackendUrl() + this.authUrl;
    return this.http.post(`${url}/login`, loginDto);
  }

  refreshToken(refreshToken: any) : any {
    let url = '';
    url = this.configService.getBackendUrl() + this.authUrl;
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${refreshToken}`)
      .set('Anonymous', 'true')
    return this.http.get(`${url}/refresh`, { 'headers': headers });
  }
}

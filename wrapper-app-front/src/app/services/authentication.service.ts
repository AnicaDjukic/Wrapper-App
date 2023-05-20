import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginDto } from '../dtos/LoginDto';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private loginUrl = "http://localhost:8080/auth/login"

  constructor(private http: HttpClient) { }

  login(loginDto: LoginDto) {
    return this.http.post(`${this.loginUrl}`, loginDto)
  }
}

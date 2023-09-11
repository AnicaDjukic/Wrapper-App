import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class DatabaseService {

  private databaseUrl = `/api/v1/databases`;

  constructor(private http: HttpClient, private configService: ConfigService) {
    const apiUrl = this.configService.getBackendUrl();
    this.databaseUrl = apiUrl + this.databaseUrl;
   }

  getAll() {
    return this.http.get<any>(`${this.databaseUrl}`);
  }

  getAllUnblocked() {
    return this.http.get<any>(`${this.databaseUrl}/unblocked`);
  }

  switch(database: string) {
    return this.http.get<any>(`${this.databaseUrl}/switch/` + database);
  }

  post(dto: any) {
    return this.http.post<any>(`${this.databaseUrl}`, dto);
  }

  put(dto: any) {
    return this.http.put<any>(`${this.databaseUrl}`, dto);
  }
  

}
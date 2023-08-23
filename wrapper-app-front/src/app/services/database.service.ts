import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DatabaseService {

  private databaseUrl = `${environment.apiUrl}/api/v1/databases`;

  constructor(private http: HttpClient) { }

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
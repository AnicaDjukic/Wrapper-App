import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DatabaseService {

  private databaseUrl = "http://localhost:8080/mongo";

  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<any>(`${this.databaseUrl}`);
  }

  switch(database: string) {
    return this.http.get<any>(`${this.databaseUrl}/switch/` + database);
  }

  post(dto: any) {
    return this.http.post<any>(`${this.databaseUrl}/`, dto);
  }

  put(dto: any) {
    return this.http.put<any>(`${this.databaseUrl}/`, dto);
  }
  

}
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ProstorijaService {

  private prostorijeUrl = "http://localhost:8080/api/v1/prostorije"

  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<any>(`${this.prostorijeUrl}`);
  }

  post(data: any) {
    return this.http.post<any>(`${this.prostorijeUrl}`, data);
  }

  put(data: any, id: string) {
    return this.http.put<any>(`${this.prostorijeUrl}/` + id, data);
  }

  delete(id: string) {
    return this.http.delete(`${this.prostorijeUrl}/` + id);
  }
}

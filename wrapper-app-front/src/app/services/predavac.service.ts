import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PredavacService {

  private predavaciUrl = "http://localhost:8080/api/v1/predavaci"

  constructor(private http: HttpClient) { }

  getAll(page: number, size: number) {
    return this.http.get<any>(`${this.predavaciUrl}?page=` + page + `&size=` + size);
  }

  post(data: any) {
    return this.http.post<any>(`${this.predavaciUrl}`, data);
  }

  put(data: any, id: string) {
    return this.http.put<any>(`${this.predavaciUrl}/` + id, data);
  }

  delete(id: string) {
    return this.http.delete(`${this.predavaciUrl}/` + id);
  }

  getAllKatedra() {
    return this.http.get<any>("http://localhost:8080/api/v1/katedre");
  }

  getAllDepartman() {
    return this.http.get<any>("http://localhost:8080/api/v1/departmani");
  }
}

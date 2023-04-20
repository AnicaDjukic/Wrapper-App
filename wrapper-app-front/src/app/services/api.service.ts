import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private predmetiUrl = "http://localhost:8080/api/v1/predmeti"

  constructor(private http: HttpClient) { }

  getAll(page: number, size: number) {
    return this.http.get<any>(`${this.predmetiUrl}?page=` + page + `&size=` + size);
  }

  get(id: string) {
    return this.http.get<any>(`${this.predmetiUrl}/` + id);
  }

  getByStudijskiProgram(studijskiProgram: string) {
    return this.http.get<any>(`${this.predmetiUrl}/studijski-program/` + studijskiProgram);
  }

  post(data: any) {
    return this.http.post<any>(`${this.predmetiUrl}`, data);
  }

  put(data: any, id: string) {
    return this.http.put<any>(`${this.predmetiUrl}/` + id, data);
  }

  delete(id: string) {
    return this.http.delete(`${this.predmetiUrl}/` + id);
  }

  getAllStudijskiProgram() {
    return this.http.get<any>("http://localhost:8080/api/v1/studijski-programi");
  }

}

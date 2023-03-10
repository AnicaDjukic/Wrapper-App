import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StudijskiProgramService {

  private studijskiProgramiUrl = "http://localhost:8080/api/v1/studijski-programi"

  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<any>(`${this.studijskiProgramiUrl}`);
  }

  post(data: any) {
    return this.http.post<any>(`${this.studijskiProgramiUrl}`, data);
  }

  put(data: any, id: string) {
    return this.http.put<any>(`${this.studijskiProgramiUrl}/` + id, data);
  }

  delete(id: string) {
    return this.http.delete(`${this.studijskiProgramiUrl}/` + id);
  }
}

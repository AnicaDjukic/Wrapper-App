import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StudentskaGrupaService {

  private studentskeGrupeUrl = "http://localhost:8080/api/v1/studentske-grupe"

  constructor(private http: HttpClient) { }

  getAll(page: number, size: number) {
    return this.http.get<any>(`${this.studentskeGrupeUrl}?page=` + page + `&size=` + size);
  }

  post(data: any) {
    return this.http.post<any>(`${this.studentskeGrupeUrl}`, data);
  }

  put(data: any, id: string) {
    return this.http.put<any>(`${this.studentskeGrupeUrl}/` + id, data);
  }

  delete(id: string) {
    return this.http.delete(`${this.studentskeGrupeUrl}/` + id);
  }
}

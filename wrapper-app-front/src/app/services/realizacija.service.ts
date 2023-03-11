import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RealizacijaService {

  private studentskeGrupeUrl = "http://localhost:8080/api/v1/realizacije/studijski-programi"

  constructor(private http: HttpClient) { }

  get(studijskiProgramId: string) {
    return this.http.get<any>(`${this.studentskeGrupeUrl}/` + studijskiProgramId);
  }
}

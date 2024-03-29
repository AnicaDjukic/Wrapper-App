import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class StudentskaGrupaService {

  private studentskeGrupeUrl = `/api/v1/studentske-grupe`;

  constructor(private http: HttpClient, private configService: ConfigService) {
    const apiUrl = this.configService.getBackendUrl();
    this.studentskeGrupeUrl = apiUrl + this.studentskeGrupeUrl;
   }
  getAll(page: number, size: number) {
    return this.http.get<any>(`${this.studentskeGrupeUrl}?page=` + page + `&size=` + size);
  }

  search(page: number, size: number, searchParams: { oznaka: string; godina: string; brojStudenata: string; studijskiProgram: string; }) {
    return this.http.get<any>(`${this.studentskeGrupeUrl}/search?page=` + page + `&size=` + size 
    + `&oznaka=` + searchParams.oznaka
    + `&godina=`+ searchParams.godina
    + `&brojStudenata=` + searchParams.brojStudenata
    + '&studProg=' + searchParams.studijskiProgram);
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

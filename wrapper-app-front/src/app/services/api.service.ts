import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private predmetiUrl = `/api/v1/predmeti`;

  constructor(private http: HttpClient, private configService: ConfigService) { 
    const apiUrl = this.configService.getBackendUrl();
    this.predmetiUrl = apiUrl + this.predmetiUrl
  }

  getAll(page: number, size: number) {
    return this.http.get<any>(`${this.predmetiUrl}?page=` + page + `&size=` + size);
  }

  search(page: number, size: number, searchParams: { oznaka: string; naziv: string; studijskiProgram: string; }) {
    return this.http.get<any>(`${this.predmetiUrl}/search?page=` + page + `&size=` + size 
    + `&oznaka=` + searchParams.oznaka
    + `&naziv=`+ searchParams.naziv
    + '&studProg=' + searchParams.studijskiProgram);
  }

  get(id: string) {
    return this.http.get<any>(`${this.predmetiUrl}/` + id);
  }

  getByStudijskiProgram(studijskiProgram: string) {
    return this.http.get<any>(`${this.predmetiUrl}/studijski-program/` + studijskiProgram + `?uRealizaciji=false`);
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

}

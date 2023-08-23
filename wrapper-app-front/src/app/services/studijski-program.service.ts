import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class StudijskiProgramService {
  
  private studijskiProgramiUrl = `${environment.apiUrl}/api/v1/studijski-programi`

  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<any>(`${this.studijskiProgramiUrl}`);
  }

  search(searchParams: { oznaka: string; naziv: string; stepenStudija: string; }) {
    return this.http.get<any>(`${this.studijskiProgramiUrl}/search?` 
    + `&oznaka=` + searchParams.oznaka
    + `&naziv=`+ searchParams.naziv
    + `&stepenStudija=` + searchParams.stepenStudija);
  }

  put(data: any, id: string) {
    return this.http.put<any>(`${this.studijskiProgramiUrl}/` + id, data);
  }
}

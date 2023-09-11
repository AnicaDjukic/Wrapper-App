import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class StudijskiProgramService {

  private studijskiProgramiUrl = `/api/v1/studijski-programi`

  constructor(private http: HttpClient, private configService: ConfigService) {
    const apiUrl = this.configService.getBackendUrl();
    this.studijskiProgramiUrl = apiUrl + this.studijskiProgramiUrl;
  }
  
  getAll() {
    return this.http.get<any>(`${this.studijskiProgramiUrl}`);
  }

  search(searchParams: { oznaka: string; naziv: string; stepenStudija: string; }) {
    return this.http.get<any>(`${this.studijskiProgramiUrl}/search?`
      + `&oznaka=` + searchParams.oznaka
      + `&naziv=` + searchParams.naziv
      + `&stepenStudija=` + searchParams.stepenStudija);
  }

  put(data: any, id: string) {
    return this.http.put<any>(`${this.studijskiProgramiUrl}/` + id, data);
  }
}

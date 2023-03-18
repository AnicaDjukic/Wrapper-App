import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RealizacijaService {

  private realizacijaUrl = "http://localhost:8080/api/v1/realizacije/studijski-programi"

  constructor(private http: HttpClient) { }

  get(studijskiProgramId: string) {
    return this.http.get<any>(`${this.realizacijaUrl}/` + studijskiProgramId);
  }

  post(studijskiProgramId : string, realizacijaPredmetDto: any) {
    return this.http.post<any>(`${this.realizacijaUrl}/` + studijskiProgramId + '/predmeti', realizacijaPredmetDto);
  }

  delete(studijskiProgramId: string, predmetId: string) {
    return this.http.delete<any>(`${this.realizacijaUrl}/` + studijskiProgramId + '/predmeti/' + predmetId);
  }
}

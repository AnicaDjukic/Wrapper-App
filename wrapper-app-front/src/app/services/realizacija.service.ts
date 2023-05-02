import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RealizacijaService {

  private realizacijaUrl = "http://localhost:8080/api/v1/realizacije"

  constructor(private http: HttpClient) { }

  get(studijskiProgramId: string) {
    return this.http.get<any>(`${this.realizacijaUrl}/studijski-programi/` + studijskiProgramId);
  }

  addPredmet(studijskiProgramId : string, realizacijaPredmetDto: any) {
    return this.http.post<any>(`${this.realizacijaUrl}/studijski-programi/` + studijskiProgramId + '/predmeti', realizacijaPredmetDto);
  }

  updatePredmet(studijskiProgramId: string, predmetId: string, realizacijaPredmetDto: any) {
    return this.http.put<any>(`${this.realizacijaUrl}/studijski-programi/` + studijskiProgramId + '/predmeti/' + predmetId, realizacijaPredmetDto);
  }

  delete(studijskiProgramId: string, predmetId: string) {
    return this.http.delete<any>(`${this.realizacijaUrl}/studijski-programi/` + studijskiProgramId + '/predmeti/' + predmetId);
  }

  deletePredmet(predmetId: string) {
    return this.http.delete<any>(`${this.realizacijaUrl}/predmeti/` + predmetId);
  }

  deletePredavac(predavacId: string) {
    return this.http.delete<any>(`${this.realizacijaUrl}/predavaci/` + predavacId);
  }

  deleteStudijskiProgram(studijskiProgramId: string) {
    return this.http.delete<any>(`${this.realizacijaUrl}/studijski-programi/` + studijskiProgramId);
  }
}

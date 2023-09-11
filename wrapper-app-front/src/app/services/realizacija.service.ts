import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class RealizacijaService {

  private realizacijaUrl = `/api/v1/realizacija`;

  constructor(private http: HttpClient, private configService: ConfigService) {
    const apiUrl = this.configService.getBackendUrl();
    this.realizacijaUrl = apiUrl + this.realizacijaUrl;
   }
  get(studijskiProgramId: string) {
    return this.http.get<any>(`${this.realizacijaUrl}/studijski-programi/` + studijskiProgramId + '/predmeti');
  }

  addStudijskiProgram(data: any) {
    return this.http.post<any>(`${this.realizacijaUrl}/studijski-programi`, data);
  }

  addPredmet(studijskiProgramId : string, realizacijaPredmetDto: any) {
    return this.http.post<any>(`${this.realizacijaUrl}/studijski-programi/` + studijskiProgramId + '/predmeti', realizacijaPredmetDto);
  }

  updatePredmet(studijskiProgramId: string, predmetId: string, predavaciDto: any) {
    return this.http.put<any>(`${this.realizacijaUrl}/studijski-programi/` + studijskiProgramId + '/predmeti/' + predmetId, predavaciDto);
  }

  delete(studijskiProgramId: string, predmetId: string) {
    return this.http.delete<any>(`${this.realizacijaUrl}/studijski-programi/` + studijskiProgramId + '/predmeti/' + predmetId);
  }

  deleteStudijskiProgram(studijskiProgramId: string) {
    return this.http.delete<any>(`${this.realizacijaUrl}/studijski-programi/` + studijskiProgramId);
  }
}

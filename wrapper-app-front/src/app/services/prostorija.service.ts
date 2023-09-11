import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class ProstorijaService {

  private prostorijeUrl = `/api/v1/prostorije`;

  constructor(private http: HttpClient, private configService: ConfigService) {
    const apiUrl = this.configService.getBackendUrl();
    this.prostorijeUrl = apiUrl + this.prostorijeUrl;
   }

  getAll(page: number, size: number) {
    return this.http.get<any>(`${this.prostorijeUrl}?page=` + page + `&size=` + size);
  }

  search(page: number, size: number, searchParams: { oznaka: string; tip: string; sekundarniTip: string, kapacitet: string; orgJedinica: string; }) {
    return this.http.get<any>(`${this.prostorijeUrl}/search?page=` + page + `&size=` + size 
    + `&oznaka=` + searchParams.oznaka
    + `&tip=`+ searchParams.tip
    + `&sekundarniTip=` + searchParams.sekundarniTip
    + `&kapacitet=` + searchParams.kapacitet
    + '&orgJed=' + searchParams.orgJedinica);
  }

  post(data: any) {
    return this.http.post<any>(`${this.prostorijeUrl}`, data);
  }

  put(data: any, id: string) {
    return this.http.put<any>(`${this.prostorijeUrl}/` + id, data);
  }

  delete(id: string) {
    return this.http.delete(`${this.prostorijeUrl}/` + id);
  }
}

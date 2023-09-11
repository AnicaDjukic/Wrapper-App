import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class PredavacService {
  
  private predavaciUrl = `/api/v1/predavaci`;

  constructor(private http: HttpClient, private configService: ConfigService) {
    const apiUrl = this.configService.getBackendUrl();
    this.predavaciUrl = apiUrl + this.predavaciUrl;
   }
  getAll(page: number, size: number) {
    return this.http.get<any>(`${this.predavaciUrl}?page=` + page + `&size=` + size);
  }

  search(page: any, size: any, searchParams: { oznaka: string; ime: string; prezime: string; orgJedinica: string; }) {
    return this.http.get<any>(`${this.predavaciUrl}/search?page=` + page + `&size=` + size 
    + `&oznaka=` + searchParams.oznaka
    + `&ime=`+ searchParams.ime
    + `&prezime=` + searchParams.prezime
    + '&orgJed=' + searchParams.orgJedinica);
  }

  post(data: any) {
    return this.http.post<any>(`${this.predavaciUrl}`, data);
  }

  put(data: any, id: string) {
    return this.http.put<any>(`${this.predavaciUrl}/` + id, data);
  }

  delete(id: string) {
    return this.http.delete(`${this.predavaciUrl}/` + id);
  }
}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class RasporedService {

  private url = `/api/v1/raspored`;

  constructor(private http: HttpClient, private configService: ConfigService) {
    const apiUrl = this.configService.getBackendUrl();
    this.url = apiUrl + this.url;
   }
  generate(id: string) {
    return this.http.post(`${this.url}/generate/${id}`, null);
  }

  send(id:string) {
    return this.http.post(`${this.url}/send/${id}`, null);
  }

  stop() {
    return this.http.put(`${this.url}/generate/stop`, null);
  }

}

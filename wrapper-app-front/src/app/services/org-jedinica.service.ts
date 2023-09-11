import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class OrgJedinicaService {

  private organizacioneJediniceUrl = `/api/v1/organizacione-jedinice`;

  constructor(private http: HttpClient, private configService: ConfigService) {
    const apiUrl = this.configService.getBackendUrl();
    this.organizacioneJediniceUrl = apiUrl + this.organizacioneJediniceUrl;
   }
  getAll() {
    return this.http.get<any>(`${this.organizacioneJediniceUrl}`);
  }
}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OrgJedinicaService {


  private organizacioneJediniceUrl = `${environment.apiUrl}/api/v1/organizacione-jedinice`;
  
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<any>(`${this.organizacioneJediniceUrl}`);
  }
}

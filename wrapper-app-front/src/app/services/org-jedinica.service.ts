import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class OrgJedinicaService {


  private organizacioneJediniceUrl = "http://localhost:8080/api/v1/organizacione-jedinice";
  
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<any>(`${this.organizacioneJediniceUrl}`);
  }
}

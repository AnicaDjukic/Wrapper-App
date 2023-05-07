import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class OrgJedinicaService {


  private katedreUrl = "http://localhost:8080/api/v1/katedre";
  private departmaniUrl = "http://localhost:8080/api/v1/departmani";
  
  constructor(private http: HttpClient) { }

  getAllKatedra() {
    return this.http.get<any>(`${this.katedreUrl}`);
  }

  getAllDepartman() {
    return this.http.get<any>(`${this.departmaniUrl}`);
  }
}

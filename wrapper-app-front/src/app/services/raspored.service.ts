import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RasporedService {

  private url = "http://localhost:8080/api/v1/raspored"

  constructor(private http: HttpClient) { }

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

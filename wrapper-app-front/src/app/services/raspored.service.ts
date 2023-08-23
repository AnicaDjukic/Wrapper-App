import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RasporedService {

  private url = `${environment.apiUrl}/api/v1/raspored`

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

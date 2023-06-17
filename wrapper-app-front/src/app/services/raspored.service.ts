import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RasporedService {

  private url = "http://localhost:8080/api/v1/raspored"

  constructor(private http: HttpClient) { }

  download(file: string | undefined): Observable<Blob> {
    return this.http.get(`${this.url}/download/${file}`, {
      responseType: 'blob'
    });
  }

  generate(id: string) {
    return this.http.post(`${this.url}/generate/${id}`, null);
  }

}

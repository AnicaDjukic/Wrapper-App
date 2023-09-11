// src/app/services/config.service.ts

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  private config: any;

  constructor(private http: HttpClient) { }

  loadConfig(): Promise<any> {
    return this.http.get('/assets/config.json').toPromise().then((config: any) => {
      this.config = config;
      console.log('Configuration loaded:', this.config);
    }).catch((error) => {
      console.error(error);
    });
  }

  getBackendUrl(): any {
    return this.config?.backendUrl;
  }
}

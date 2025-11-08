import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class DashboardService {

  private apiUrl = 'http://localhost:9090/api/statistics'; // adapte l'URL si besoin

  constructor(private http: HttpClient, private auth: AuthService) {}

  getStats(): Observable<any> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.get<any>(this.apiUrl, { headers });
  }
}

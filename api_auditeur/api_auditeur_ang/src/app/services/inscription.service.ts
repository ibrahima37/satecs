import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InscriptionService {
  private apiUrl = "http://192.168.1.184:8087/api/inscription";

  constructor(private http: HttpClient){}

  soumettreInscription(payload: any): Observable<any>{
    return this.http.post(this.apiUrl, payload);
  }

}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Establishment } from 'src/app/models/establishment.model';

@Injectable({
  providedIn: 'root'
})
export class EditEstablishmentService {

  constructor(
    private readonly http: HttpClient,
  ) { }

  getCEP(cep: string): Observable<any> {
    return this.http.get<any>(`https://viacep.com.br/ws/${cep}/json/`);
  }

  updateEstablishment(id: number, est: Establishment): Observable<any> {
    return this.http.put(`http://localhost:8080/restaurant/id=${id}`, est);
  }

  deleteEstablishment(id: number): Observable<any>{
    return this.http.delete(`http://localhost:8080/restaurant/${id}`);
  }
}

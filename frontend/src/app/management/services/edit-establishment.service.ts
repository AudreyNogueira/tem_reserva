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

  deleteEstablishment(id: number): Observable<any> {
    return this.http.delete(`http://localhost:8080/restaurant/${id}`);
  }

  getEstablishmentById(id: number): Observable<Establishment> {
    return this.http.get<Establishment>(`http://localhost:8080/restaurant/id=${id}`);
  }

  createEstablishment(est: any): Observable<any> {
    return this.http.post('http://localhost:8080/restaurant/create', est);
  }

  setImage(image: any): Observable<any> {
    return this.http.post('http://localhost:8080/restaurant/upload', image);
  }

  deleteImage(idImage: number): Observable<any> {
    return this.http.delete(`http://localhost:8080/restaurant/image/${idImage}`);
  }
}

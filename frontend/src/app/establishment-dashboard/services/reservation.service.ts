import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Reserve } from '../../models/reserve.model';
@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  constructor(
    private readonly http: HttpClient,
  ) { }

  makeReservation(reserve: Reserve): Observable<any> {
    return this.http.post<any>('http://localhost:8080/reserve/create', reserve);
  }
}

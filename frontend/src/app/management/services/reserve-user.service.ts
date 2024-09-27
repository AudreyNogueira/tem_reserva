import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReserveUserService {

  constructor(
    private readonly http: HttpClient,
  ) { }

  cancelReservation(id: number): Observable<any> {
    return this.http.delete<any>(`http://localhost:8080/reserve/${id}`);
  }

  getReservesByUserId(id: number): Observable<any> {
    return this.http.get<any>(`http://localhost:8080/reserve/userId=${id}`);
  }
}

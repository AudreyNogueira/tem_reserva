import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Reserve, ReservePerDay } from 'src/app/models/reserve.model';

@Injectable({
  providedIn: 'root'
})
export class ReserveEstablishmentService {

  constructor(
    private readonly http: HttpClient,
  ) { }

  getReservesByUserEstablishmentId(id: string): Observable<ReservePerDay[]> {
    return this.http.get<any>(`http://localhost:8080/reserve/restaurantId=${id}`);
  }

  cancelReservation(id: number): Observable<any> {
    return this.http.delete<any>(`http://localhost:8080/reserve/${id}`);
  }

  editReservation(r: Reserve): Observable<any> {
    const reserve = {
      amountOfPeople: r.amountOfPeople,
      confirmed: r.confirmed,
      idRestaurant: null,
      idUser: r.user.id,
      observation: r.observation,
      period: r.period,
      reserveDate: r.reserveDate,
    };
    return this.http.put<any>(`http://localhost:8080/reserve/id=${r.id}`, reserve);
  }
}

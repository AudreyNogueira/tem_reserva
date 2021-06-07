import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ReservePerDay } from 'src/app/models/reserve.model';

@Injectable({
  providedIn: 'root'
})
export class ReserveEstablishmentService {

  constructor(
    private readonly http: HttpClient,
  ) { }

  getReservesByUserEstablishmentId(id: string): Observable<ReservePerDay[]> {
    return this.http.get<any>(`http://localhost:8080/reserve/restaurantId=${id}`).pipe(map(r => {
      const response = [];
      r.forEach(reserve => response.push(this.mapReservePerDay(reserve)));
      return response;
    }));
  }

  mapReservePerDay(reserve: any): ReservePerDay {
    return {
      day: reserve.day,
      maxNumberOfPeople: reserve.maxNumberOfPeople,
      reserves: reserve.reserves,
      currentPeople: this.mapCurrentPeople(reserve.currentPeople),
    }
  }

  mapCurrentPeople(period: any[]) {
    let count = 0;
    period.forEach(p => count += p.currentPeople);
    return count;
  }

  cancelReservation(id: number): Observable<any> {
    return this.http.delete<any>(`http://localhost:8080/reserve/${id}`);
  }
}

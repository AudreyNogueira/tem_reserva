import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { Establishment, EstablishmentZone, Zone } from '../../models/establishment.model';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { Periodo } from 'src/app/models/time.model';

@Injectable({
  providedIn: 'root'
})
export class EstablishmentListService {

  loadMock$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(null);
  reserve$: Subject<boolean> = new Subject<boolean>();

  constructor(
    private readonly http: HttpClient,
  ) { }

  loadEstablishmentList(): Observable<EstablishmentZone> {
    return this.http.get<EstablishmentZone>('http://localhost:8080/restaurant/home').pipe(map(e => {
      return this.mapCurrentPeopleNow(e);
    }));
  }

  loadZoneEstablishment(zone: string): Observable<Zone> {
    return this.http.get<Zone>(`http://localhost:8080/restaurant/zone=${zone}`);
  }

  getEstablishmentById(id: number): Observable<Establishment> {
    return this.http.get<Establishment>(`http://localhost:8080/restaurant/id=${id}`);
  }

  mapCurrentPeopleNow(est: EstablishmentZone) {
    est.topRated.forEach(tp => tp.currentPeople =
      tp?.currentPeople.find(p => p.period === this.getPeriod())?.currentPeople ? tp?.currentPeople.find(p => p.period === this.getPeriod())?.currentPeople : 0);
    est.zone.forEach(z => z.restaurantList.forEach(tp => tp.currentPeople =
      tp?.currentPeople.find(p => p.period === this.getPeriod())?.currentPeople ? tp?.currentPeople.find(p => p.period === this.getPeriod())?.currentPeople : 0));
    return est;
  }

  getPeriod(): string {
    const now = moment(new Date()).get('hour');
    if (now < 6) {
      return Periodo.MADRUGADA;
    }
    if (now >= 6 && now < 12) {
      return Periodo.MANHA;
    }
    if (now >= 12 && now < 18) {
      return Periodo.TARDE;
    }
    return Periodo.NOITE;
  }

}

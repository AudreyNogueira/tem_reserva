import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Establishment, EstablishmentZone, Zone } from '../../models/establishment.model';

@Injectable({
  providedIn: 'root'
})
export class EstablishmentListService {

  loadMock$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(null);

  constructor(
    private readonly http: HttpClient,
  ) { }

  loadEstablishmentList(): Observable<EstablishmentZone> {
    return this.http.get<EstablishmentZone>('http://localhost:8080/restaurant/home');
  }

  loadZoneEstablishment(zone: string): Observable<Zone> {
    return this.http.get<Zone>(`http://localhost:8080/restaurant/zone=${zone}`);
  }

  getEstablishmentById(id: number): Observable<Establishment> {
    return this.http.get<Establishment>(`http://localhost:8080/restaurant/id=${id}`);
  }

}

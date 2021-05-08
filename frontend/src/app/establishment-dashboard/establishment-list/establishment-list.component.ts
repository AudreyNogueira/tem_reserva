import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Establishment, Zone } from 'src/app/models/establishment.model';
import { RoutesEnum } from 'src/app/models/routes.enum';
import { ZoneEnum } from 'src/app/models/zone.enum';

@Component({
  selector: 'establishment-list',
  templateUrl: './establishment-list.component.html',
  styleUrls: ['./establishment-list.component.scss']
})
export class EstablishmentListComponent implements OnInit {

  @Input() zone: Zone;
  @Input() param?: string;
  zoneRoute = RoutesEnum.ESTABLISHMENTS_DASHBOARD + RoutesEnum.ZONE;
  zoneEnum = ZoneEnum;


  constructor(
    private readonly router: Router,
  ) { }

  ngOnInit(): void {
  }

  navigateToEstablishment(est: Establishment) {
    this.router.navigateByUrl(`${RoutesEnum.ESTABLISHMENTS_DASHBOARD}/selecionado/${est.id}/${est.restaurantName}`);
  }

}

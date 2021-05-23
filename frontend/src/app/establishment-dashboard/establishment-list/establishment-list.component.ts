import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
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
  @Input() showFilter?: boolean;
  zoneRoute = RoutesEnum.ESTABLISHMENTS_DASHBOARD + RoutesEnum.ZONE;
  zoneEnum = ZoneEnum;
  customList: Establishment[];
  search = '';

  constructor(
    private readonly router: Router,
  ) { }

  ngOnInit(): void {
    this.customList = this.zone.restaurantList;
  }

  navigateToEstablishment(est: Establishment): void {
    this.router.navigateByUrl(`${RoutesEnum.ESTABLISHMENTS_DASHBOARD}/selecionado/${est.id}/${est.restaurantName}`);
  }

  searchRestaurant() {
    if (!this.search) this.customList = this.zone.restaurantList;
    else this.customList = this.zone.restaurantList.filter(e => e.restaurantName.toLowerCase().includes(this.search.toLocaleLowerCase()));

    console.log(this.customList);

  }

}

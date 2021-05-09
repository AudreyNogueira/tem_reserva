import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { take } from 'rxjs/operators';
import { Zone } from '../../models/establishment.model';
import { EstablishmentListService } from '../services/establishment-list.service';

@Component({
  selector: 'zone',
  templateUrl: './zone.component.html',
  styleUrls: ['./zone.component.scss']
})
export class ZoneComponent implements OnInit {

  zoneResponse: Zone;
  zone: string;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly establishmentListService: EstablishmentListService,
  ) { }

  ngOnInit(): void {
    window.scroll(0, 0);

    const param = this.route.snapshot.paramMap.get('name');
    this.zone = 'Zona ' + param.charAt(0).toUpperCase() + param.slice(1);

    this.establishmentListService.loadZoneEstablishment(this.zone).pipe(take(1)).subscribe(z => this.zoneResponse = z);
  }

}

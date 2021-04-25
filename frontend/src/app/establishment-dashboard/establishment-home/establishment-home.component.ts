import { Component, OnInit } from '@angular/core';
import { EstablishmentZone } from '../../models/establishment.model';
import { EstablishmentListService } from '../services/establishment-list.service';
import { take } from 'rxjs/operators';

@Component({
  selector: 'establishment-home',
  templateUrl: './establishment-home.component.html',
  styleUrls: ['./establishment-home.component.scss']
})
export class EstablishmentHomeComponent implements OnInit {

  establishmentsResponse: EstablishmentZone;

  constructor(
    private readonly establishmentListService: EstablishmentListService,
  ) { }

  ngOnInit(): void {

    this.establishmentListService.loadMock$.subscribe(() => {
      this.establishmentListService.loadEstablishmentList().pipe(take(1)).subscribe(list => this.establishmentsResponse = list);
    })

  }


}

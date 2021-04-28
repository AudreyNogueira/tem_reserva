import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EstablishmentDashboardComponent } from './establishment-dashboard.component';
import { EstablishmentListComponent } from './establishment-list/establishment-list.component';
import { TopRatedComponent } from './top-rated/top-rated.component';
import { EstablishmentHomeComponent } from './establishment-home/establishment-home.component';
import { EstablishmentDashboardRoutingModule } from './establishment-dashboard-routing.module';
import { ZoneComponent } from './zone/zone.component';
import { EstablishmentComponent } from './establishment/establishment.component';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';


@NgModule({
  declarations: [
    EstablishmentDashboardComponent,
    EstablishmentListComponent,
    TopRatedComponent,
    EstablishmentHomeComponent,
    ZoneComponent,
    EstablishmentComponent,
  ],
  imports: [
    CommonModule,
    EstablishmentDashboardRoutingModule,
    BsDatepickerModule.forRoot(),
  ],
})
export class EstablishmentDashboardModule { }

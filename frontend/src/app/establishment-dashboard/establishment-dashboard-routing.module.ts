import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { EstablishmentHomeComponent } from './establishment-home/establishment-home.component';
import { EstablishmentComponent } from './establishment/establishment.component';
import { ZoneComponent } from './zone/zone.component';

const routes: Routes = [
  {
    path: '',
    component: EstablishmentHomeComponent,
    pathMatch: 'full'
  },
  {
    path: 'zona/:name',
    component: ZoneComponent,
  },
  {
    path: 'selecionado/:establishment',
    component: EstablishmentComponent,
  },
  {
    path: '**',
    redirectTo: '',
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EstablishmentDashboardRoutingModule { }

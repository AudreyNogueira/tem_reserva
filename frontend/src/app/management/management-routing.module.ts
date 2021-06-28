import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { EditEstablishmentComponent } from './edit-establishment/edit-establishment.component';
import { EditUserComponent } from './edit-user/edit-user.component';
import { ManagementComponent } from './management.component';
import { ReserveEstablishmentComponent } from './reserve-establishment/reserve-establishment.component';
import { ReserveUserComponent } from './reserve-user/reserve-user.component';

const routes: Routes = [
  {
    path: '',
    component: ManagementComponent,
    children: [
      {
        path: 'editar-usuario',
        component: EditUserComponent,
      },
      {
        path: 'editar-estabelecimento',
        component: EditEstablishmentComponent,
      },
      {
        path: 'reserva',
        children: [
          {
            path: 'estabelecimento',
            component: ReserveEstablishmentComponent,
          },
          {
            path: 'cliente',
            component: ReserveUserComponent,
          }
        ]
      },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ManagementRoutingModule { }

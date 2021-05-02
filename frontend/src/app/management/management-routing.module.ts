import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { EditEstablishmentComponent } from './edit-establishment/edit-establishment.component';
import { EditUserComponent } from './edit-user/edit-user.component';
import { ManagementComponent } from './management.component';

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
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ManagementRoutingModule { }

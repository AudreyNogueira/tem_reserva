import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterEstablishmentComponent } from './register/register-establishment/register-establishment.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'cadastrar-estabelecimento',
    component: RegisterEstablishmentComponent
  },
  {
    path: 'estabelecimentos',
    loadChildren: () => import('../app/establishment-dashboard/establishment-dashboard.module').then(m => m.EstablishmentDashboardModule),
  },
  {
    path: '**',
    redirectTo: 'estabelecimentos'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

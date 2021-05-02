import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
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
    loadChildren: () => import('./establishment-dashboard/establishment-dashboard.module').then(m => m.EstablishmentDashboardModule),
  },
  {
    path: 'perfil',
    loadChildren: () => import('./management/management.module').then(m => m.ManagementModule),
  },
  {
    path: 'sobre',
    component: HomeComponent
  },
  {
    path: '**',
    redirectTo: 'sobre',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

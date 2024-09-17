import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { RegisterEstablishmentComponent } from './register/register-establishment/register-establishment.component';
import { RegisterClientComponent } from './register/register-client/register-client.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/sobre',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'cadastrar-estabelecimento',
    component: RegisterEstablishmentComponent,
  },
  {
    path: 'estabelecimentos',
    loadChildren: () => import('./establishment-dashboard/establishment-dashboard.module').then(m => m.EstablishmentDashboardModule),
    canActivate: [AuthGuard],
  },
  {
    path: 'perfil',
    loadChildren: () => import('./management/management.module').then(m => m.ManagementModule),
    canActivate: [AuthGuard],
  },
  {
    path: 'sobre',
    component: HomeComponent,
  },
  {
    path: 'cadastrar-cliente',
    component: RegisterClientComponent,
  },
  {
    path: '**',
    redirectTo: 'sobre',
    pathMatch: 'full'
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

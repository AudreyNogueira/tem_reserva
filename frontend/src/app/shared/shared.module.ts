import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MenuLoggedComponent } from './menu-logged/menu-logged.component';
import { RouterModule } from '@angular/router';


@NgModule({
  imports: [
    CommonModule,
    RouterModule,
  ],
  declarations: [
    MenuLoggedComponent,
  ],
  exports: [
    MenuLoggedComponent,
  ]
})
export class SharedModule { }

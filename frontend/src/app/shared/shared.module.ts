import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MenuLoggedComponent } from './menu-logged/menu-logged.component'


@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    MenuLoggedComponent,
  ],
  exports: [
    MenuLoggedComponent,
  ]
})
export class SharedModule { }

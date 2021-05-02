import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ManagementRoutingModule } from './management-routing.module';
import { ManagementComponent } from './management.component';
import { SharedModule } from '../shared/shared.module';
import { EditUserComponent } from './edit-user/edit-user.component';
import { ReactiveFormsModule } from '@angular/forms';
import { TextMaskModule } from 'angular2-text-mask';
import { EditEstablishmentComponent } from './edit-establishment/edit-establishment.component';


@NgModule({
  declarations: [
    ManagementComponent,
    EditUserComponent,
    EditEstablishmentComponent,
  ],
  imports: [
    CommonModule,
    ManagementRoutingModule,
    SharedModule,
    ReactiveFormsModule,
    TextMaskModule,
  ]
})
export class ManagementModule { }

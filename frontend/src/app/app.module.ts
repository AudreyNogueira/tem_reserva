import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MenuComponent } from './menu/menu.component';
import { CollapseModule } from 'ngx-bootstrap/collapse';
import { ModalModule } from 'ngx-bootstrap/modal';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './login/login.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ModalLoginComponent } from './modals/modal-login/modal-login.component';
import { RegisterEstablishmentComponent } from './register/register-establishment/register-establishment.component';
import { HttpClientModule } from '@angular/common/http';
import { SharedModule } from './shared/shared.module';
import { FooterComponent } from './footer/footer.component';
import { HomeComponent } from './home/home.component';
import { TextMaskModule } from 'angular2-text-mask';
import { ModalConfirmComponent } from './modals/modal-confirm/modal-confirm.component';
import { RegisterClientComponent } from './register/register-client/register-client.component';
import { ModalReserveComponent } from './modals/modal-reserve/modal-reserve.component';
import { ModalFeedbackComponent } from './modals/modal-feedback/modal-feedback.component';

@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    LoginComponent,
    ModalLoginComponent,
    RegisterEstablishmentComponent,
    FooterComponent,
    HomeComponent,
    ModalConfirmComponent,
    RegisterClientComponent,
    ModalReserveComponent,
    ModalFeedbackComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CollapseModule.forRoot(),
    BrowserAnimationsModule,
    ModalModule.forRoot(),
    ReactiveFormsModule,
    HttpClientModule,
    SharedModule,
    TextMaskModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

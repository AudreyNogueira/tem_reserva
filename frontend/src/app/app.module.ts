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

@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    LoginComponent,
    ModalLoginComponent,
    RegisterEstablishmentComponent,
    FooterComponent,
    HomeComponent,
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

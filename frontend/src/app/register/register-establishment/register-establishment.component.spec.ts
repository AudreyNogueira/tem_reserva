import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterEstablishmentComponent } from './register-establishment.component';

describe('RegisterEstablishmentComponent', () => {
  let component: RegisterEstablishmentComponent;
  let fixture: ComponentFixture<RegisterEstablishmentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RegisterEstablishmentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterEstablishmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReserveEstablishmentComponent } from './reserve-establishment.component';

describe('ReserveEstablishmentComponent', () => {
  let component: ReserveEstablishmentComponent;
  let fixture: ComponentFixture<ReserveEstablishmentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReserveEstablishmentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReserveEstablishmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

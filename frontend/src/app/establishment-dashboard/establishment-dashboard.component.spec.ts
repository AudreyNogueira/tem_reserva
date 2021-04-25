import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EstablishmentDashboardComponent } from './establishment-dashboard.component';

describe('EstablishmentDashboardComponent', () => {
  let component: EstablishmentDashboardComponent;
  let fixture: ComponentFixture<EstablishmentDashboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EstablishmentDashboardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EstablishmentDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

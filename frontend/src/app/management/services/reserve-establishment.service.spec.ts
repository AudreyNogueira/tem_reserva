import { TestBed } from '@angular/core/testing';

import { ReserveEstablishmentService } from './reserve-establishment.service';

describe('ReserveEstablishmentService', () => {
  let service: ReserveEstablishmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReserveEstablishmentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

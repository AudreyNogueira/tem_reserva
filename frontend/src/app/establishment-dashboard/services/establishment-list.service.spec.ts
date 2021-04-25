import { TestBed } from '@angular/core/testing';

import { EstablishmentListService } from './establishment-list.service';

describe('EstablishmentListService', () => {
  let service: EstablishmentListService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EstablishmentListService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

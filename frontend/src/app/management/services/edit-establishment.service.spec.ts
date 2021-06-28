import { TestBed } from '@angular/core/testing';

import { EditEstablishmentService } from './edit-establishment.service';

describe('EditEstablishmentService', () => {
  let service: EditEstablishmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EditEstablishmentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

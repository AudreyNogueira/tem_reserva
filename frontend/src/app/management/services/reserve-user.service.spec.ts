import { TestBed } from '@angular/core/testing';

import { ReserveUserService } from './reserve-user.service';

describe('ReserveUserService', () => {
  let service: ReserveUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReserveUserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

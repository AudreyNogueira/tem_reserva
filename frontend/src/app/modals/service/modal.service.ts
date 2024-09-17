import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { ModalData } from '../../models/modal-data.model';

@Injectable({
  providedIn: 'root'
})
export class ModalService {

  $openModal = new Subject<ModalData>();
  $comunication = new Subject<any>();

  constructor() { }
}

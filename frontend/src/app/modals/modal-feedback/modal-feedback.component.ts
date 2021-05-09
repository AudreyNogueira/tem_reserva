import { Component, Input, OnInit } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';

@Component({
  selector: 'modal-feedback',
  templateUrl: './modal-feedback.component.html',
  styleUrls: ['./modal-feedback.component.scss']
})
export class ModalFeedbackComponent implements OnInit {

  @Input() type?: string;
  @Input() message: string;

  constructor(
    readonly modalService: BsModalService,
  ) { }

  ngOnInit(): void {
  }

}

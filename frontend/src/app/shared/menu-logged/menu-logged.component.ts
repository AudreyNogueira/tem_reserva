import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
  selector: 'menu-logged',
  templateUrl: './menu-logged.component.html',
  styleUrls: ['./menu-logged.component.scss']
})
export class MenuLoggedComponent implements OnInit {

  @Output() close = new EventEmitter();

  constructor() { }

  ngOnInit(): void {
  }

  closeMenu() {
    this.close.emit();
  }

}

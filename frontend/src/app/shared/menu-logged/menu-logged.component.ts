import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { RoutesEnum } from 'src/app/models/routes.enum';

@Component({
  selector: 'menu-logged',
  templateUrl: './menu-logged.component.html',
  styleUrls: ['./menu-logged.component.scss']
})
export class MenuLoggedComponent implements OnInit {

  @Output() closeMenu = new EventEmitter();
  routesEnum = RoutesEnum;

  constructor() { }

  ngOnInit(): void {
  }

  /**
   * Emite evento para fechar o menu
   */
  close(): void {
    this.closeMenu.emit();
  }

}

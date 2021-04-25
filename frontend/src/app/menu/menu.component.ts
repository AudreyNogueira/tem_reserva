import { Component, HostListener, OnInit } from '@angular/core';
import { RoutesEnum } from '../models/routes.enum';

@Component({
  selector: 'menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  isCollapsed = true;
  routes = RoutesEnum;

  constructor() { }

  /**
   * Fecha o menu caso esteja aberto e ocorrá um redimensionamento da tela
   */
  @HostListener('window:resize', ['$event'])
  onResize(): void {
    if (window.innerWidth > 768) this.isCollapsed = true;
  }

  ngOnInit(): void {
  }

  /**
   * Fecha o menu caso esteja aberto (método só funcionará quando a resolução for de tablet ou smartphones)
   * @method closeMenu
   */
  closeMenu(): void {
    if (!this.isCollapsed) this.isCollapsed = !this.isCollapsed;
  }
}

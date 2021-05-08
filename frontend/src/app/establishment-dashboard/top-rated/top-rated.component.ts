import { Component, ElementRef, Input, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { Router } from '@angular/router';
import { Establishment } from 'src/app/models/establishment.model';
import { RoutesEnum } from 'src/app/models/routes.enum';

@Component({
  selector: 'top-rated',
  templateUrl: './top-rated.component.html',
  styleUrls: ['./top-rated.component.scss']
})
export class TopRatedComponent implements OnInit {

  @ViewChild('slide') slide: ElementRef<HTMLElement>;
  @ViewChildren('element') element: QueryList<ElementRef<HTMLElement>>;
  @Input() carouselElements: Establishment[];
  @Input() param?: string;
  zone: string;

  constructor(
    private readonly router: Router,
  ) { }

  ngOnInit(): void {
    if (this.param) this.zone = this.param + ' | ';
  }

  /**
   * Método para rolar o carrossel pelas setas
   * @method navigateCarousel
   * @param direction direção para qual o carrossel vai
   */
  navigateCarousel(direction: string): void {
    direction === 'next' ?
      this.slide.nativeElement.scrollBy({ left: this.element.last.nativeElement.scrollWidth + 50 }) :
      this.slide.nativeElement.scrollBy({ left: -this.element.last.nativeElement.scrollWidth + 50 });
  }

  navigateToEstablishment(est: Establishment): void {
    this.router.navigateByUrl(`${RoutesEnum.ESTABLISHMENTS_DASHBOARD}/selecionado/${est.id}/${est.restaurantName}`);
  }

}

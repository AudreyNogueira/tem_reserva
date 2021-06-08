import { Component, ElementRef, Input, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';

@Component({
  selector: 'home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  @ViewChild('slide') slide: ElementRef<HTMLElement>;
  @ViewChildren('element') element: QueryList<ElementRef<HTMLElement>>;
  // @Input() carouselElements: Establishment[];
  @Input() param?: string;
  zone: string;

  constructor() { }

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
}
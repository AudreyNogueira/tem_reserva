import { Component, OnInit } from '@angular/core';
import { defineLocale } from 'ngx-bootstrap/chronos';
import { BsLocaleService } from 'ngx-bootstrap/datepicker';
import { ptBrLocale } from 'ngx-bootstrap/locale';
defineLocale('pt-br', ptBrLocale);

@Component({
  selector: 'establishment',
  templateUrl: './establishment.component.html',
  styleUrls: ['./establishment.component.scss']
})
export class EstablishmentComponent implements OnInit {

  bsInlineValue = new Date().toLocaleDateString('pt-br');
  minDate = new Date();
  images: string[];
  indexImages = 0;

  constructor(
    private localeService: BsLocaleService
  ) {
    this.localeService.use('pt-br');
  }

  ngOnInit(): void {

    this.images = [
      '../../../assets/images/examples/lenha.png',
      '../../../assets/images/examples/pizza.png',
      '../../../assets/images/examples/massa.png',
      '../../../assets/images/examples/pizza-zoom.png',
    ];
  }

  /**
   * Método para rotacionar o array de imagens
   * @param reverse caso `true` altera o sentido ("para trás")
   */
  rotate(reverse?: boolean): void {
    if (reverse) this.images.unshift(this.images.pop());
    else this.images.push(this.images.shift());
  }

  log() {
  }

}

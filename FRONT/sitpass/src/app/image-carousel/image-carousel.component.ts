import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-image-carousel',
  templateUrl: './image-carousel.component.html',
  styleUrls: ['./image-carousel.component.css']
})
export class ImageCarouselComponent {
  @Input() images: string[] = [];
  currentIndex = 0;

  showNextImage() {
    this.currentIndex = (this.currentIndex + 1) % this.images.length;
  }

  showPreviousImage() {
    this.currentIndex = (this.currentIndex - 1 + this.images.length) % this.images.length;
  }
}

import { Component, EventEmitter, Input, OnChanges, OnInit, Output } from '@angular/core';
import { UtilServiceService } from '../services/util-service.service';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent implements OnInit, OnChanges {
  @Input() totalItems!: number;
  @Input() pageSize!: number;
  @Output() pageSelected = new EventEmitter<number>();
  pages: number[] = [];
  activePage: number = 1;

  constructor(private utilService: UtilServiceService) {}

  ngOnInit() {
    this.calculatePages();
  }

  ngOnChanges() {
    this.calculatePages();
  }

  calculatePages() {
    this.pages = [];
    const totalPages = this.utilService.getNoPages(this.totalItems, this.pageSize);
    for (let i = 1; i <= totalPages; i++) {
      this.pages.push(i);
    }
  }

  selected(newPage: number) {
    if (newPage >= 1 && newPage <= this.pages.length) {
      this.activePage = newPage;
      this.pageSelected.emit(this.activePage);
    }
  }
}

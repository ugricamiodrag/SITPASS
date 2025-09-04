import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UtilServiceService {

  constructor() { }

  public getNoPages(totalItems: number, pageSize: number): number {
		return Math.ceil(totalItems / pageSize);
	}
}

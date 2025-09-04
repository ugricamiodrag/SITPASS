import {Component, EventEmitter, Output} from '@angular/core';
import {dayOfWeek, Facility} from "../models/facility.model";
import {FacilityService} from "../services/facility.service";

@Component({
  selector: 'app-search-facility',
  templateUrl: './search-facility.component.html',
  styleUrl: './search-facility.component.css'
})
export class SearchFacilityComponent {

  @Output() searchResults = new EventEmitter<Facility[]>();

  searchCriteria: any = {
    cities: [],
    disciplines: [],
    minRating: null,
    maxRating: null,
    workDaySearchCriteriaList: []
  };

  cityField: String = "";
  disciplineField: String = "";

  daysOfWeek = Object.values(dayOfWeek);

  constructor(private facilityService: FacilityService) {}

  onSearch(): void {

    if (this.cityField.length > 0) {
      this.searchCriteria.cities = this.cityField.split(',')
        .map(city => city.trim());
    }
    else {
      this.searchCriteria.cities = [];
    }



    if (this.disciplineField.length > 0) {
      this.searchCriteria.disciplines = this.disciplineField.split(',')
        .map(discipline => discipline.trim());
    }
    else {
      this.searchCriteria.disciplines = [];
    }



    console.log(this.searchCriteria.workDaySearchCriteriaList);
    console.log(this.searchCriteria);
    this.facilityService.searchFacilities(this.searchCriteria).subscribe((results: Facility[]) => {
      this.searchResults.emit(results);
      console.log(results);
    });
  }

  addWorkDayCriteria(): void {
    this.searchCriteria.workDaySearchCriteriaList.push({
      dayOfWeek: '',
      startTime: '',
      endTime: ''
    });
  }

  removeWorkDayCriteria(index: number): void {
    this.searchCriteria.workDaySearchCriteriaList.splice(index, 1);
  }


}

import {WorkDaySearchCriteria} from "./dto/WorkDayDTO.model";

export class FacilitySearchCriteria {
  cities: string[] = [];
  disciplines: string[] = [];
  minRating?: number;
  maxRating?: number;
  workDaySearchCriteriaList: WorkDaySearchCriteria[] = [];

  constructor(cities: string[], disciplines: string[], minRating?: number, maxRating?: number, workDaySearchCriteriaList?: WorkDaySearchCriteria[]) {
    this.cities = cities;
    this.disciplines = disciplines;
    this.minRating = minRating;
    this.maxRating = maxRating;
    this.workDaySearchCriteriaList = [];
  }
}

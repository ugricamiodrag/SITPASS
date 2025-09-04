import {dayOfWeek} from "../facility.model";

export class WorkDaySearchCriteria {
  day: dayOfWeek;
  startTime: string;
  endTime: string;

  constructor(dayOfWeek: dayOfWeek, startTime: string, endTime: string) {
    this.day = dayOfWeek;
    this.startTime = startTime;
    this.endTime = endTime;
  }
}

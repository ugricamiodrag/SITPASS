
export class AnalyticsDTO {
  facilityId: number;
  from: Date;
  to: Date;


  constructor(facilityId: number, from: Date, to: Date) {
    this.facilityId = facilityId;
    this.from = from;
    this.to = to;
  }
}

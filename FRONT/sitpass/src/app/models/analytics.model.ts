export class AnalyticsData {
  date: Date;
  usersCount: number;
  reviewsCount: number;


  constructor(date: Date, usersCount: number, reviewsCount: number) {
    this.date = date;
    this.usersCount = usersCount;
    this.reviewsCount = reviewsCount;
  }
}

export class TimePeriodData {
  period: Date;
  usersCount: number;


  constructor(period: Date, usersCount: number) {
    this.period = period;
    this.usersCount = usersCount;
  }
}

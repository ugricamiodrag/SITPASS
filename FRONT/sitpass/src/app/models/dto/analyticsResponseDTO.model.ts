import {AnalyticsData, TimePeriodData} from "../analytics.model";

export class AnalyticsResponseDTO {
  analyticsData: AnalyticsData[];
  timePeriodData: TimePeriodData[];


  constructor(analyticsData: AnalyticsData[], timePeriodData: TimePeriodData[]) {
    this.analyticsData = analyticsData;
    this.timePeriodData = timePeriodData;
  }
}

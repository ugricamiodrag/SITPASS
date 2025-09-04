import { Injectable } from '@angular/core';
import {AnalyticsDTO} from "../models/dto/analyticsDTO.model";
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {ConfigService} from "./config.service";
import {AnalyticsData, TimePeriodData} from "../models/analytics.model";
import {map} from "rxjs/operators";
import {AnalyticsResponseDTO} from "../models/dto/analyticsResponseDTO.model";

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {

  constructor(private http: HttpClient, private config: ConfigService) {
  }

  getCustomAnalyticsData(analytics: AnalyticsDTO): Observable<AnalyticsResponseDTO> {
    return this.http.post<AnalyticsResponseDTO>(this.config.customAnalytics, analytics).pipe(
      map(response => {
        return new AnalyticsResponseDTO(
          response.analyticsData.map(data => new AnalyticsData(data.date, data.usersCount, data.reviewsCount)),
          response.timePeriodData.map(tpd => new TimePeriodData(tpd.period, tpd.usersCount))
        );
      })
    );

  }

  getTimePeriodData(analytics: AnalyticsDTO): Observable<TimePeriodData[]> {
    return this.http.post<TimePeriodData[]>(this.config.userAnalytics, analytics);
  }

  getAnalyticsData(analytics: AnalyticsDTO): Observable<AnalyticsData[]> {
    return this.http.post<AnalyticsData[]>(this.config.reviewsAnalytics, analytics)
  }
}

import {Component, Inject, OnInit} from '@angular/core';
import { ChartOptions, ChartType, ChartDataset } from 'chart.js';
import { NgChartsModule } from 'ng2-charts';
import { AnalyticsService } from '../services/analytics.service';
import { AnalyticsData, TimePeriodData } from '../models/analytics.model';
import { FormBuilder, FormGroup } from '@angular/forms';
import {AnalyticsDTO} from "../models/dto/analyticsDTO.model";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {timeRangeValidator} from "../validators/timeRangeValidator.validator";
import {dateRangeValidator} from "../validators/dateRange.validator";

@Component({
  selector: 'app-analytics',
  templateUrl: './analytics.component.html',
  styleUrls: ['./analytics.component.css']
})
export class AnalyticsComponent implements OnInit {
  facilityId: number;
  analyticsData: AnalyticsData[] = [];
  timePeriodData: TimePeriodData[] = [];


  public barChartOptions: ChartOptions = {
    responsive: true,
  };
  public barChartLabels: string[] = [];
  public barChartType: ChartType = 'bar';
  public barChartLegend = true;
  public barChartPlugins = [];

  public barChartData: ChartDataset[] = [
    { data: [], label: 'Number of Users' },
    { data: [], label: 'Number of Reviews' }
  ];

  public lineChartData: ChartDataset[] = [
    { data: [], label: 'Number of Users' }
  ];
  public lineChartLabels: string[] = [];

  customDateForm: FormGroup;

  constructor(private analyticsService: AnalyticsService, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: { facilityId: number }) {
    this.facilityId = data.facilityId;
    this.customDateForm = this.fb.group({
      startDate: ['', [dateRangeValidator]],
      endDate: ['', [dateRangeValidator]]
    });
  }

  ngOnInit(): void {
    this.loadAnalyticsData('weekly');
    this.loadTimePeriodData('daily');
  }

  loadAnalyticsData(interval: 'weekly' | 'monthly' | 'yearly'): void {
    let beginningDate = this.getBeginningDate(interval)
    let analytics = new AnalyticsDTO(this.facilityId, beginningDate, new Date());
    this.analyticsService.getAnalyticsData(analytics).subscribe(data => {
      this.analyticsData = data;
      this.barChartLabels = data.map(d => this.formatDate(d.date));
      this.barChartData[0].data = data.map(d => d.usersCount);
      this.barChartData[1].data = data.map(d => d.reviewsCount);
    });
  }

  loadTimePeriodData(interval: 'daily' | 'weekly' | 'monthly'): void {
    let beginningDate = this.getBeginningDate(interval)
    let analytics = new AnalyticsDTO(this.facilityId, beginningDate, new Date());
    this.analyticsService.getTimePeriodData(analytics).subscribe(data => {
      this.timePeriodData = data;
      this.lineChartLabels = data.map(d => this.formatDate(d.period));
      this.lineChartData[0].data = data.map(d => d.usersCount);
    });
  }

  onCustomDateSubmit(): void {
    const startDate = this.customDateForm.value.startDate;
    const endDate = this.customDateForm.value.endDate;
    let analytics = new AnalyticsDTO(this.facilityId, startDate, endDate);

    this.analyticsService.getCustomAnalyticsData(analytics).subscribe(response => {
      this.analyticsData = response.analyticsData;
      this.timePeriodData = response.timePeriodData;
      this.barChartLabels = this.analyticsData.map(d => this.formatDate(d.date));
      this.barChartData[0].data = this.analyticsData.map(d => d.usersCount);
      this.barChartData[1].data = this.analyticsData.map(d => d.reviewsCount);
      this.lineChartLabels = this.timePeriodData.map(d => this.formatDate(d.period));
      this.lineChartData[0].data = this.timePeriodData.map(d => d.usersCount);
    });
  }



  getBeginningDate(interval: string): Date {
    const currentDate = new Date();
    switch (interval) {
      case "daily":
        const twentyFourHoursAgo = currentDate.getTime() - (24 * 60 * 60 * 1000);
        currentDate.setTime(twentyFourHoursAgo);
        break;
      case "weekly":
        currentDate.setDate(currentDate.getDate() - 7);
        break;
      case "monthly":
        currentDate.setMonth(currentDate.getMonth() - 1);
        break;
      case "yearly":
        currentDate.setFullYear(currentDate.getFullYear() - 1);
        break;
      default:
        throw new Error("Invalid interval");
    }
    return currentDate;
  }

  private formatDate(date: Date | string): string {
    if (!(date instanceof Date)) {
      date = new Date(date);
    }

    if (!(date instanceof Date) || isNaN(date.getTime())) {
      throw new Error('Invalid date');
    }

    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
  }



}

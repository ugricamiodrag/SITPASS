import { Component } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {ConfigService} from "../services/config.service";
import {FacilityIndex} from "../models/facility-index.model";


@Component({
  selector: 'app-new-search',
  templateUrl: './new-search.component.html',
  styleUrls: ['./new-search.component.css'],
})
export class NewSearchComponent {
  // SIMPLE SEARCH state
  simpleQuery: string = '';
  simpleRanges: any = {
    reviewCount: { min: null, max: null },
    avgEquipmentGrade: { min: null, max: null },
    avgStaffGrade: { min: null, max: null },
    avgHygieneGrade: { min: null, max: null },
    avgSpaceGrade: { min: null, max: null },
  };

  // ADVANCED SEARCH state
  advancedQuery: string = '';
  advRanges: any = {
    reviewCount: { min: null, max: null },
    avgEquipmentGrade: { min: null, max: null },
    avgStaffGrade: { min: null, max: null },
    avgHygieneGrade: { min: null, max: null },
    avgSpaceGrade: { min: null, max: null },
  };

  numericFields = [
    { name: 'reviewCount', label: 'Reviews count' },
    { name: 'avgEquipmentGrade', label: 'Avg Equipment Grade' },
    { name: 'avgStaffGrade', label: 'Avg Staff Grade' },
    { name: 'avgHygieneGrade', label: 'Avg Hygiene Grade' },
    { name: 'avgSpaceGrade', label: 'Avg Space Grade' },
  ];

  // RESULTS
  results: FacilityIndex[] = [];
//
  constructor(private http: HttpClient,  private config: ConfigService) {}

  // SIMPLE SEARCH
  onSimpleSearch() {
    const ranges: any = {};
    for (const key of Object.keys(this.simpleRanges)) {
      const min = this.simpleRanges[key].min;
      const max = this.simpleRanges[key].max;
      if (min != null || max != null) {
        ranges[key] = { min: min ?? null, max: max ?? null }; // <-- send object, not string
      }
    }

    const payload = {
      keywords: this.simpleQuery.split(' ').filter(k => k.trim() !== ''),
      expression: [],
      ranges
    };

    this.http.post<any>(this.config.simple_search_url, payload, {
      headers: { 'Content-Type': 'application/json' }
    }).subscribe({
      next: (res) => {
        console.log("✅ Response from backend:", res);
        this.results = res.content;
      },
      error: (err) => console.error("❌ Error occurred:", err),
      complete: () => console.log("Request completed")
    });
  }

  // ADVANCED SEARCH
  onAdvancedSearch() {
    const ranges: any = {};
    for (const key of Object.keys(this.advRanges)) {
      const min = this.advRanges[key].min;
      const max = this.advRanges[key].max;
      if (min != null || max != null) {
        ranges[key] = `${min ?? '*'}..${max ?? '*'}`;
      }
    }

    const payload = {
      expression: this.advancedQuery.split(' ').filter((e) => e.trim() !== ''),
      ranges,
    };

    this.http.post<any[]>(this.config.advanced_search_url, payload).subscribe((res) => {
      this.results = res;
    });
  }
}

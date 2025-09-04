import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';
import { Observable } from 'rxjs';
import { Facility } from '../models/facility.model';
import {HttpClient, HttpParams} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FacilityService {

  constructor(private config: ConfigService, private http: HttpClient) { }

  getFacilities(): Observable<Facility[]> {
    return this.http.get<Facility[]>(this.config.facility_url)

  }

  getFacility(id: number): Observable<Facility> {
    return this.http.get<Facility>(this.config.getFacilityByIdUrl(id));
  }

  createFacility(formData: FormData): Observable<Facility> {
    return this.http.post<Facility>(this.config.create_facility_url, formData);
  }

  updateFacility(id: number, formData: FormData): Observable<Facility> {
    return this.http.put<Facility>(this.config.getUpdateFacilityUrl(id), formData);
  }

  deleteFacility(id: number): Observable<void> {
    return this.http.delete<void>(this.config.getDeleteFacilityUrl(id));
  }

  hideFacility(id: number): Observable<void> {
    return this.http.put<void>(this.config.getHideFacilityUrl(id), {});
  }

  searchFacilities(criteria: any) : Observable<Facility[]> {
    console.log("Difference between criteria: " + criteria + "\n and JSON: " + JSON.stringify(criteria));
    return this.http.post<Facility[]>(this.config.searchFacilityUrl, criteria)
  }

  getPopularFacilities(): Observable<Facility[]> {
    return this.http.get<Facility[]>(this.config.popularFacility_url);
  }

  getVisitedFacilities(): Observable<Facility[]> {
    return this.http.get<Facility[]>(this.config.visitedFacility_url);
  }

  getUnvisitedFacilities(page: number, size: number): Observable<any> {
    const requestBody = {
      page: page,
      pageSize: size
    };
    return this.http.post<any>(this.config.unvisitedFacility_url, requestBody);
  }

  getManagedFacilities(): Observable<Facility[]> {
    return this.http.get<Facility[]>(this.config.managedFacility_url);
  }

}

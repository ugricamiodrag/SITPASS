import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ConfigService} from "./config.service";
import {ReviewDTO} from "../models/dto/ReviewDTO.model";
import {Observable} from "rxjs";
import {ReviewPageDTO} from "../models/dto/ReviewPageDTO.model";

@Injectable({
  providedIn: 'root'
})
export class ReviewsService {

  constructor(private http: HttpClient, private config: ConfigService) { }

  saveReview(reviewDTO: ReviewDTO): Observable<any> {
    return this.http.post(this.config.reviews_url, reviewDTO);
  }

  getReviewsByFacility(id: number): Observable<ReviewPageDTO[]> {
    return this.http.get<ReviewPageDTO[]>(this.config.getReviewsForFacilityUrl(id));
  }

  getReviewsByUser(id: number): Observable<ReviewPageDTO[]> {
    return this.http.get<ReviewPageDTO[]>(this.config.getReviewForUserUrl(id));
  }

  deleteReviewByUser(id: number): Observable<any> {
    return this.http.delete<Observable<any>>(this.config.getReviewForUserUrl(id));
  }

  hideReview(id: number, hide: boolean): Observable<any> {
    console.log(hide);
    return this.http.put<any>(this.config.getReviewsForFacilityUrl(id), {hide : hide});
  }
}

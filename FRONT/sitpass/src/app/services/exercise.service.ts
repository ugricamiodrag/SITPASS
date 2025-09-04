import { Injectable } from '@angular/core';
import {ConfigService} from "./config.service";
import {Facility} from "../models/facility.model";
import {User} from "../models/user.model";
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs/operators";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ExerciseService {

  constructor(private config: ConfigService, private http: HttpClient) { }

  hasBeenInFacility(facility: Facility, userId: number): Observable<boolean> {
    let id = facility.id;
    console.log("Facility: " + facility.name + "\nwith id: " + facility.id);
    return this.http.post<number>(this.config.exercises_number_url, { id : id, userId : userId }).pipe(
      map(numberOfExercises => numberOfExercises > 0)
    );
  }
}

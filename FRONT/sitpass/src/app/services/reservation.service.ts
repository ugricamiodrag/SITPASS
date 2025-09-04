import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Exercise } from '../models/exercise.model';
import { ConfigService } from './config.service';
import { User } from '../models/user.model';
import {ExerciseToSendDTO} from "../models/dto/exerciseDTO.model";

@Injectable({
  providedIn: 'root'
})
export class ReservationService {


  constructor(private http: HttpClient, private config: ConfigService) {}

  createReservation(reservation: ExerciseToSendDTO): Observable<ExerciseToSendDTO> {
    return this.http.post<ExerciseToSendDTO>(this.config.createReservationUrl, reservation);
  }

  getUserByEmail(email: string): Observable<User> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<User>(this.config.getUserByEmail, { email }, { headers });
  }
}

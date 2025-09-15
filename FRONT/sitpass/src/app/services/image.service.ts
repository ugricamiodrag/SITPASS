import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {ConfigService} from "./config.service";

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  private apiUrl = 'http://localhost:8080/api/images';

  constructor(private http: HttpClient, private config: ConfigService) { }

  getImage(filename: string): Observable<Blob> {
    return this.http.get(this.config.getFileGetter(filename), { responseType: 'blob' });
  }
}

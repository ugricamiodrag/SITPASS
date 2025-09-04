import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  private apiUrl = 'http://localhost:8080/api/images';

  constructor(private http: HttpClient) { }

  getImage(filename: string): Observable<Blob> {
    const sanitizedFilename = filename.replace(/\\/g, '/');
    const url = `${this.apiUrl}/serve`;
    return this.http.post(url, sanitizedFilename, { responseType: 'blob' });
  }
}

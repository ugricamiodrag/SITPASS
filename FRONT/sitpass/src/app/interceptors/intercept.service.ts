import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class Interceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    const item = localStorage.getItem('user');

    if (item) {
      try {
        const decodedItem = JSON.parse(item);
        req = req.clone({
          setHeaders: {
            Authorization: `Bearer ${decodedItem.token}` 
          }
        });
      } catch (error) {
        console.error('Error parsing token:', error);
        
      }
    }

    return next.handle(req);
  }
}
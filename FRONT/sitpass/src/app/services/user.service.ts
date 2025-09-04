import {Injectable} from '@angular/core';
import {ConfigService} from "./config.service";
import {Observable, of, switchMap} from "rxjs";
import {User} from "../models/user.model";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {UserDTO} from "../models/profile.model";
import {Manager} from "../models/managers.model";
import {AuthenticationService} from "./authentication.service";
import {catchError, map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private config: ConfigService, private http: HttpClient, private authService: AuthenticationService) { }

  getUserByEmail(email: string): Observable<User> {
    return this.http.post<User>(this.config.getUserByEmail, {email: email});
  }

  updateUser(id: number, request: UserDTO): Observable<User> {
    let headers = new HttpHeaders();
    headers = headers.set('Content-Type', 'application/json');

    return this.http.put<User>(this.config.getUpdateUserUrl(id), request, { headers : headers});
  }

  updateUserPhoto(id: number, photo: FormData): Observable<User> {

    return this.http.put<User>(this.config.getUpdatePhotoUrl(id), photo);
  }

  getUsers(): Observable<User[]> {
    return this.http.post<User[]>(this.config.users_url + '/all', {});
  }

  getManager(id: number) : Observable<Manager> {
    console.log("Get manager: " + id);
    return this.http.get<Manager>(this.config.manages_url + "/" + id);

  }

  setManager(newManager: Manager) : Observable<Manager> {
    let headers = new HttpHeaders();
    headers = headers.set('Content-Type', 'application/json');
    const payload = {
      user: { email: newManager.user.email },
      startDate: newManager.startDate,
      facility: newManager.facilityId
    };
    console.log(payload);
    return this.http.post<Manager>(this.config.getManagerByFacilityId(newManager.facilityId!), newManager);
  }

  removeManager(manager: Manager) {
    console.log(manager);
    return this.http.put<Manager>(this.config.getManagerByFacilityId(manager.facilityId!), manager);
  }

  isUserManagerForThisFacility(facilityId: number): Observable<boolean> {
    const email = this.authService.getEmailFromToken();
    if (!email) {
      return of(false);
    }

    return this.getUserByEmail(email).pipe(
      switchMap(user =>
        this.getManager(facilityId).pipe(
          map(manager => manager.user.email === user.email),
          catchError(() => of(false))
        )
      ),
      catchError(() => of(false))
    );
  }

  getUser(): Observable<User> | null {
    let email = this.authService.getEmailFromToken();
    if (email) {
      return this.getUserByEmail(email);
    }
    else {
      return null;
    }

  }
}

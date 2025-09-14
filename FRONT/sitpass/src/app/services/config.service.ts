import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  private _api_url = 'http://localhost:8080/api';
  private _user_url = this._api_url + '/users';
  private _search_url = this._api_url + '/search';
  private _login_url = this._user_url + '/login';
  private _index_url = this._api_url + '/index';
  private _file_url = this._api_url + '/file';

  getFileGetter(nm: string): string {
    return this._file_url + '/' + nm;
  }




  getIndexUrl(id: any): string {
    return this._index_url + "/" + id;
  }



  get searchUrl(): string {
    return this._search_url;
  }

  private _simple_search_url = this.searchUrl + "/simple"
  private _advanced_search_url = this.searchUrl + "/advance";
  private _mlt_search_url = this.searchUrl + "/mlt";


  get mlt_search_url(): string {
    return this._mlt_search_url;
  }

  get simple_search_url(): string {
    return this._simple_search_url;
  }

  get advanced_search_url(): string {
    return this._advanced_search_url;
  }

  get users_url(): string {
    return this._user_url;
  }

  get login_url(): string {
    return this._login_url;
  }

  private _register_url = this._user_url + '/register';

  get register_url(): string {
    return this._register_url;
  }

  private _check_email_url = this._user_url + '/check-email';

  private _getUserByMail_url = this._user_url + '/email';

  get getUserByEmail(): string {
    return this._getUserByMail_url;
  }

  getUpdateUserUrl(id: number): string {
    return this._user_url + '/' + id;
  }

  getUpdatePhotoUrl(id: number): string {
    return this.getUpdateUserUrl(id) + '/image'
  }



  checkEmailUrl(email: string): string {
      return `${this._check_email_url}?email=${encodeURIComponent(email)}`;
  }

  private _logout_url = this._user_url + '/logOut';

  get logout_url(): string {
    return this._logout_url;
  }

  private _change_password_url = this._user_url + '/change-password';

  get change_password_url(): string {
    return this._change_password_url;
  }

  private _request_url = this._api_url + '/requests';

  get requests_url(): string{
    return this._request_url;
  }


  private _decline_url = this._request_url + '/decline';

  get decline_url(): string{
    return this._decline_url;
  }

  private _accept_url = this._request_url + '/accept';

  get accept_url(): string{
    return this._accept_url;
  }

  private _facility_url = this._api_url + '/facilities';

  get facility_url(): string {
    return this._facility_url;
  }

  get popularFacility_url(): string {
    return this._facility_url + "/popular";
  }

  get visitedFacility_url(): string {
    return this._facility_url + "/visited";
  }

  get unvisitedFacility_url(): string {
    return this._facility_url + "/unvisited";
  }

  get managedFacility_url(): string {
    return this._facility_url + "/managed";
  }


  private _manages_url = this._api_url + '/manages';

  get manages_url(): string {
    return this._manages_url;
  }

  getManagesById(id: number): string {
    return this.manages_url + '/' + id;
  }

  getManagerByFacilityId(id: number): string {
    return this._facility_url + "/manager/" + id ;
  }

  private _create_facility_url = this._facility_url + '/create';

  get create_facility_url(): string {
    return this._facility_url;
  }

  getUpdateFacilityUrl(id: number): string {
    return `${this._facility_url}/${id}`;
  }

  getDeleteFacilityUrl(id: number): string {
    return `${this._facility_url}/${id}`;
  }

  getFacilityByIdUrl(id: number): string {
    return `${this._facility_url}/${id}`;
  }

  getHideFacilityUrl(id: number): string {
    return `${this._facility_url}/hide/${id}`;
  }

  private _searchFacilityUrl = this._facility_url + '/search';

  get searchFacilityUrl(): string {
    return this._searchFacilityUrl;
  }

  private _exercises_url = this._api_url + '/exercises';

  get createReservationUrl(): string{
    return this._exercises_url;
  }

  private _exercises_number_url = this._exercises_url + '/number'

  get exercises_number_url(): string {
    return this._exercises_number_url;
  }

  private _reviews_url = this._api_url + "/reviews";

  get reviews_url(): string {
    return this._reviews_url;
  }

  getReviewsForFacilityUrl(id: number): string {
    return this._reviews_url + '/' + id;
  }

  getReviewForUserUrl(id: number): string {
    return this._reviews_url + '/user/' + id;
  }

  private _comments_url = this._api_url + '/comments';

  getCommentsUrl(id: number): string {
    return this._comments_url + '/' + id;
  }

  get commentsUrl(): string {
    return this._comments_url;
  }

  getUserIdFromComment(id: number): string {
    return this._comments_url + '/user/' + id;
  }

  private _analyticsUrl = this._api_url + '/analytics';

  private _userAnalytics = this._analyticsUrl + '/users';

  get userAnalytics(): string {
    return this._userAnalytics;
  }

  private _reviewsAnalyticsUrl = this._analyticsUrl + '/reviews';

  get reviewsAnalytics(): string {
    return this._reviewsAnalyticsUrl;
  }

  private _customAnalyticsUrl = this._analyticsUrl + '/custom';

  get customAnalytics(): string {
    return this._customAnalyticsUrl;
  }

}

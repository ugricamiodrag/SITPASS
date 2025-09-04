import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './routing/app-routing.module';
import { AppComponent } from './app.component';
import { RegistrationFormComponent } from './registration-form/registration-form.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './login/login.component';
import { PaginationComponent } from './pagination/pagination.component';
import { Interceptor } from './interceptors/intercept.service';
import { HomeComponent } from './home/home.component';
import { NavbarAdminComponent } from './core/navbar-admin/navbar-admin.component';
import { NavbarUserComponent } from './core/navbar-user/navbar-user.component';
import { NavbarManagerComponent } from './core/navbar-manager/navbar-manager.component';
import { ProfileComponent } from './profile/profile.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { AccountRequestComponent } from './account-request/account-request.component';
import { RequestsComponent } from './requests/requests.component';
import { FacilityFormModalComponent } from './facility-form-modal/facility-form-modal.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { FacilityCardComponent } from './facility-card/facility-card.component';
import { MatDialogActions, MatDialogModule, MatDialogContent } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FacilitiesComponent } from './facilities/facilities.component';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { ReservationFormComponent } from './reservation-form/reservation-form.component';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule, MAT_DATE_LOCALE } from '@angular/material/core';
import { MatButton, MatIconButton } from '@angular/material/button';
import { IvyCarouselModule } from 'angular-responsive-carousel2';
import { ImageCarouselComponent } from './image-carousel/image-carousel.component';
import { ProfileInformationComponent } from './profile-information/profile-information.component';
import { NgOptimizedImage } from '@angular/common';
import { AddManagerComponent } from './add-manager/add-manager.component';
import { SearchFacilityComponent } from './search-facility/search-facility.component';
import { RatingSystemComponent } from './rating-system/rating-system.component';
import {StarRatingConfigService, StarRatingModule} from 'angular-star-rating';
import {CustomStarConfigService} from "./services/custom-star-config.service";
import { CustomRatingComponent } from './custom-rating/custom-rating.component';
import { StarRatingComponent } from './star-rating/star-rating.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ReviewsPageComponent } from './reviews-page/reviews-page.component';
import { CommentSectionComponent } from './comment-section/comment-section.component';
import { CommentComponent } from './comment/comment.component';
import { CommentFieldComponent } from './comment-field/comment-field.component';
import { AnalyticsComponent } from './analytics/analytics.component';
import { NgChartsModule } from 'ng2-charts';


@NgModule({
  declarations: [
    AppComponent,
    RegistrationFormComponent,
    LoginComponent,
    PaginationComponent,
    HomeComponent,
    NavbarAdminComponent,
    NavbarUserComponent,
    NavbarManagerComponent,
    ProfileComponent,
    ChangePasswordComponent,
    AccountRequestComponent,
    RequestsComponent,
    FacilityFormModalComponent,
    FacilityCardComponent,
    FacilitiesComponent,
    ReservationFormComponent,
    ImageCarouselComponent,
    ProfileInformationComponent,
    AddManagerComponent,
    SearchFacilityComponent,
    RatingSystemComponent,
    CustomRatingComponent,
    StarRatingComponent,
    ReviewsPageComponent,
    CommentSectionComponent,
    CommentComponent,
    CommentFieldComponent,
    AnalyticsComponent  // Correctly declared here
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    StarRatingModule.forRoot(),
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    FontAwesomeModule,
    MatDialogModule,
    IvyCarouselModule,
    NgChartsModule,
    MatDialogActions,
    MatNativeDateModule,
    MatDialogContent,
    MatFormFieldModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatIconModule,
    ToastrModule.forRoot(),
    MatButton,
    MatIconButton,
    NgOptimizedImage
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: Interceptor, multi: true }, provideAnimationsAsync(), {
    provide: StarRatingConfigService,
    useClass: CustomStarConfigService
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }

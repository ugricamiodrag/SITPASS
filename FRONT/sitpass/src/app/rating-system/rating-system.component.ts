import {Component, Inject, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CommentDTO, RateDTO, ReviewDTO} from "../models/dto/ReviewDTO.model";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {ReviewsService} from "../services/reviews.service";
import {ToastrService} from "ngx-toastr";



@Component({
  selector: 'app-rating-system',
  templateUrl: './rating-system.component.html',
  styleUrl: './rating-system.component.css'
})
export class RatingSystemComponent implements OnInit{
  ratingForm!: FormGroup;
  facilityId: number
  userId: number;

  constructor(private fb: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public data: { facilityId: number, userId: number },
              private reviewService: ReviewsService,
              private toastr: ToastrService
              ) {
    this.facilityId = this.data.facilityId;
    this.userId = this.data.userId;
  }

  ngOnInit(): void {
    this.ratingForm = this.fb.group({
      hygiene: [null, Validators.required],
      equipment: [null, Validators.required],
      staff: [null, Validators.required],
      space: [null, Validators.required],
      comment: ['']
    });
  }





  onSubmit() {
    if (this.ratingForm.valid) {
      console.log(this.ratingForm.value);
      const form = this.ratingForm.value;
      let commentDTO;
      if (this.ratingForm.value.comment) {
        commentDTO = new CommentDTO(form.comment);
      }
      const rateDTO = new RateDTO(form.equipment, form.staff, form.hygiene, form.space);
      const reviewDTO = new ReviewDTO(this.userId, rateDTO, this.facilityId)
      if (commentDTO) {
        reviewDTO.commentDTO = commentDTO;
      }

      if (!this.userId || !this.facilityId) {
        this.toastr.error("User ID or Facility ID is missing");
        return;
      }

      console.log(reviewDTO);
      this.reviewService.saveReview(reviewDTO).subscribe(value => {
        if (value) {
          this.toastr.success("You have successfully left the review!")
          window.location.reload();
        }
        else {
          this.toastr.error("There has been an error. Try again later.")
          window.location.reload();
        }

      })
    } else {
      this.toastr.error("There has been an error. Try again later.")
      window.location.reload();
    }
  }
}

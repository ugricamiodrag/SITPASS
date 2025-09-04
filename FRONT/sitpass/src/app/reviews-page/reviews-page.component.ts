import { Component, Inject, Input, OnInit, Optional } from '@angular/core';
import { ReviewPageDTO } from '../models/dto/ReviewPageDTO.model';
import { ReviewsService } from '../services/reviews.service';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { CommentService } from '../services/comment.service';
import { CommentFieldComponent } from '../comment-field/comment-field.component';
import { CommentToSendDTO } from '../models/dto/commentDTO.model';
import { UserService } from '../services/user.service';
import { CommentSectionComponent } from '../comment-section/comment-section.component';

@Component({
  selector: 'app-reviews-page',
  templateUrl: './reviews-page.component.html',
  styleUrls: ['./reviews-page.component.css']
})
export class ReviewsPageComponent implements OnInit {
  facilityId!: number;
  reviews: ReviewPageDTO[] = [];
  sortField: string = 'createdAt';
  sortOrder: 'asc' | 'desc' = 'desc';
  @Input() userId!: number;
  isManager: boolean = false;
  numberOfRepliesMap: { [key: number]: number } = {};

  constructor(
    private reviewService: ReviewsService,
    @Optional() @Inject(MAT_DIALOG_DATA) public data: { facilityId?: number, isManager?: boolean },
    private toastr: ToastrService,
    private commentService: CommentService,
    public dialog: MatDialog,
    private userService: UserService
  ) {
    if (data) {
      this.facilityId = data.facilityId ?? this.facilityId;
      this.isManager = data.isManager ?? this.isManager;
    }
  }

  ngOnInit() {
    this.loadReviews();
    this.sortReviews();
  }

  changeSortField(field: string) {
    this.sortField = field;
    this.sortReviews();
  }

  toggleSortOrder() {
    this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
    this.sortReviews();
  }

  loadReviews(): void {
    if (this.facilityId) {
      this.reviewService.getReviewsByFacility(this.facilityId).subscribe((reviews) => {
        this.reviews = reviews;
        reviews.forEach(review => {
          this.getNumberOfReplies(review.comment?.id!);
        });

        this.sortReviews();
      });
    } else if (this.userId) {
      this.reviewService.getReviewsByUser(this.userId).subscribe((reviews) => {
        this.reviews = reviews;
        reviews.forEach(review => {
          if (review.comment && review.comment.id) {
            this.getNumberOfReplies(review.comment.id!);
          }
        });

        this.sortReviews();
      });
    } else {
      this.reviews = [];
    }
  }

  sortReviews() {
    this.reviews.sort((a, b) => {
      const aValue = this.getNestedFieldValue(a, this.sortField);
      const bValue = this.getNestedFieldValue(b, this.sortField);

      let comparison = 0;
      if (aValue > bValue) {
        comparison = 1;
      } else if (aValue < bValue) {
        comparison = -1;
      }

      return this.sortOrder === 'asc' ? comparison : -comparison;
    });
  }

  getNestedFieldValue(obj: any, path: string) {
    return path.split('.').reduce((acc, part) => acc && acc[part], obj);
  }

  deleteReview(id: number): void {
    this.reviewService.deleteReviewByUser(id).subscribe(
      () => {
        this.toastr.success('You have successfully deleted the review!');
        this.loadReviews();
      },
      (error) => {
        console.error('Error deleting review:', error);
        this.toastr.error('Failed to delete the review. Please try again later.');
      }
    );
  }

  hide(number: number, hidden: boolean) {
    this.reviewService.hideReview(number, hidden).subscribe(value => {
      if (value) {
        this.toastr.success('You have successfully hidden the review!');
      } else {
        this.toastr.error('There has been an error, try again later.');
      }
    });
  }

  getNumberOfReplies(id: number): void {
    if (id) {
      this.commentService.getComments(id).subscribe((comments) => {

        this.numberOfRepliesMap[id] = comments.replies ? comments.replies.length : 0;
      });
    } else {
      this.numberOfRepliesMap = {};
    }
  }

  managerReply(id: number, result: string) {
    this.userService.getUser()?.subscribe(value => {
      if (!value) {
        this.toastr.error('There has been an error.');
      } else {
        let dto = new CommentToSendDTO(value.id!, result, id);
        this.commentService.save(dto).subscribe(value1 => {
          if (value1) {
            this.toastr.success('You have successfully commented under this review!');
          } else {
            this.toastr.error('There has been an error, try again.');
          }
        });
      }
    });
  }

  openCommentsModal(id: number) {
    const dialogRef = this.dialog.open(CommentFieldComponent, {
      width: '250px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.managerReply(id, result);
      }
    });
  }

  seeReplies(commentId: number) {
    const dialogRef = this.dialog.open(CommentSectionComponent, {
      width: '400px',
      height: '600px',
      data: { commentId: commentId }
    });
  }
}

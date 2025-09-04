import { Component, Inject, OnInit } from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog} from "@angular/material/dialog";
import { CommentService } from "../services/comment.service";
import { CommentSectionDTO } from "../models/dto/commentSectionDTO.model";
import {UserService} from "../services/user.service";
import {User} from "../models/user.model";
import {CommentFieldComponent} from "../comment-field/comment-field.component";
import {CommentToSendDTO} from "../models/dto/commentDTO.model";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-comment-section',
  templateUrl: './comment-section.component.html',
  styleUrls: ['./comment-section.component.css']
})
export class CommentSectionComponent implements OnInit {
  commentId: number;
  comments!: CommentSectionDTO;
  replies: CommentSectionDTO[] = [];
  loggedInUserId?: number;
  commentAuthors: { [key: number]: number } = {};

  constructor(@Inject(MAT_DIALOG_DATA) public data: { commentId: number }, private commentService: CommentService, private userService: UserService, public dialog: MatDialog, private toastr: ToastrService) {
    this.commentId = data.commentId;
  }

  ngOnInit(): void {
    this.userService.getUser()!.subscribe(user => {
      this.loggedInUserId = user.id;

      this.getAllReplies();

    })

  }

  getAllReplies(): void {
    this.commentService.getComments(this.commentId).subscribe(
      (res: CommentSectionDTO) => {

        this.extractReplies(res);
      },
      (error) => {
        console.error('Error fetching comments:', error);
      }
    );
  }

  extractReplies(comment: CommentSectionDTO): void {
    this.extractRepliesTwo(comment);

    if (comment.replies && comment.replies.length > 0) {
      this.replies.push(...comment.replies);
      this.replies.forEach(reply => this.extractRepliesTwo(reply));
      comment.replies.forEach(reply => this.extractRepliesTwo(reply));
    }
  }

  extractRepliesTwo(comment: CommentSectionDTO): void {
    this.commentService.getUserIdByCommentId(comment.id).subscribe(userId => {
      this.commentAuthors[comment.id] = userId;
    });
    comment.replies?.forEach(value => this.extractRepliesTwo(value));

  }


  replyToComment(commentId: number) {
    const dialogRef = this.dialog.open(CommentFieldComponent, {
      width: '400px',

      data: {commentId : commentId}
    })
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.createReply(commentId, result);
        this.replies = [];
        this.getAllReplies();
      }
    })
  }

  createReply(id: number, result: string) {
    this.userService.getUser()?.subscribe(value => {
      if (!value) {
        this.toastr.error("There has been an error.")

      } else {
        let dto = new CommentToSendDTO(value.id!, result, id);
        this.commentService.save(dto).subscribe(value1 => {
          if (value1) {
            this.toastr.success("You have successfully commented under this review!");
          } else {
            this.toastr.error("There has been an error, try again.");
          }
        })
      }
    });
  }

  canReply(id: number): boolean {
    if (this.loggedInUserId && this.commentAuthors[id] !== undefined) {

      return this.loggedInUserId === this.commentAuthors[id];
    }
    else {
      return false;
    }


  }
}

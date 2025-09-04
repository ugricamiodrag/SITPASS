import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { CommentSectionDTO } from '../models/dto/commentSectionDTO.model';

@Component({
  selector: 'app-comment',
  template: `
    <div *ngFor="let comment of comments">
      <div class="comment-box">
        <p class="username">{{ comment.username }}</p>
        <p class="comment-text">{{ comment.comment }}</p>
        <p class="created-at">{{ comment.createdAt | date:'medium' }}</p>
        <button *ngIf="!canReply(comment.id)" (click)="replyToComment(comment.id)">Reply</button>

        <div *ngIf="comment.replies && comment.replies.length > 0" class="replies">
          <app-comment [comments]="comment.replies" [loggedInUserId]="loggedInUserId" [commentAuthors]="commentAuthors" (replyEvent)="replyToComment($event)"></app-comment>
        </div>
      </div>
    </div>
  `,
  styleUrls: ['./comment.component.css']
})
export class CommentComponent implements OnChanges {
  @Input() comments: CommentSectionDTO[] = [];
  @Input() loggedInUserId!: number;
  @Input() commentAuthors!: { [key: number]: number };
  @Output() replyEvent = new EventEmitter<number>();

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['comments'] || changes['commentAuthors']) {

    }
  }

  replyToComment(commentId: number): void {
    this.replyEvent.emit(commentId);
  }

  canReply(commentId: number): boolean {

    if (this.loggedInUserId && this.commentAuthors[commentId] !== undefined) {

      return this.loggedInUserId === this.commentAuthors[commentId];
    }
    else {
      return false;
    }
  }
}

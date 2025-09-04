import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-comment-field',
  templateUrl: './comment-field.component.html',
  styleUrl: './comment-field.component.css'
})
export class CommentFieldComponent {
  commentForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<CommentFieldComponent>,
    private fb: FormBuilder
  ) {
    this.commentForm = this.fb.group({
      comment: ['', [Validators.required, Validators.minLength(5)]]
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    if (this.commentForm.valid) {
      this.dialogRef.close(this.commentForm.value.comment);
    }
  }
}

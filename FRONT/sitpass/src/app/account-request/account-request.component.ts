import { Component, Input, Output, EventEmitter } from '@angular/core';
import { AccountRequest } from '../models/account-request.model';

@Component({
  selector: 'app-account-request',
  templateUrl: './account-request.component.html',
  styleUrls: ['./account-request.component.css']
})
export class AccountRequestComponent {
  @Input() request!: AccountRequest;
  @Output() accept = new EventEmitter<AccountRequest>();
  @Output() decline = new EventEmitter<{ request: AccountRequest, reason?: string }>();

  onAccept() {
    this.accept.emit(this.request);
  }

  onDecline() {
    let reason: string | undefined = prompt('Enter decline reason (optional):') || undefined;
    this.decline.emit({ request: this.request, reason });
  }
}

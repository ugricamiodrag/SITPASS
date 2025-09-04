export interface AccountRequest {
    _id?: number;
    email: string;
    createdAt?: string;
    address?: string;
    status: RequestStatus;
    password?: string;
    rejectionReason?: string;
  }
  
  export enum RequestStatus {
    PENDING = 'PENDING',
    APPROVED = 'APPROVED',
    REJECTED = 'REJECTED'
  }
  
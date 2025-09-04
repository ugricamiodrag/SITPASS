import { Facility } from './facility.model';
import { User } from './user.model';  // Assume you have a User model

export interface Exercise {
  id?: number;
  user: User;
  from: Date;
  until: Date; 
  facility: Facility;
}

import { User } from "./user.model";
import { Facility } from "./facility.model";
import {UserDTO} from "./dto/user-dto.model";

export class Manager {
  id?: number;
  user: UserDTO;
  startDate: Date;
  endDate?: Date;
  facilityId: number;

  constructor(
    user: UserDTO,

    startDate: Date = new Date(),
    facilityId: number,
    id?: number,
    endDate?: Date
  ) {
    this.id = id;
    this.user = user;
    this.startDate = startDate;
    this.endDate = endDate;
    this.facilityId = facilityId;
  }
}

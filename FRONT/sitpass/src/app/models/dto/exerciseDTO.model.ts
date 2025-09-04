import {from} from "rxjs";


export class ExerciseToSendDTO {
  id?: number;
  userId: number;
  from: Date;
  until: Date;
  facilityId: number;


  constructor(id: number, userId: number, from: Date, until: Date, facilityId: number) {
    this.id = id;
    this.userId = userId;
    this.from = from;
    this.until = until;
    this.facilityId = facilityId;
  }
}

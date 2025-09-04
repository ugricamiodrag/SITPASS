export class ReviewDTO {
  userId: number;
  commentDTO?: CommentDTO;
  rateDTO: RateDTO;
  facilityId: number;

  constructor(userId: number, rateDTO: RateDTO, facilityId: number, commentDTO?: CommentDTO) {
    this.userId = userId;
    this.commentDTO = commentDTO;
    this.rateDTO = rateDTO;
    this.facilityId = facilityId;
  }
}

export class CommentDTO {
  id?: number;
  text: string;
  parent?: CommentDTO;

  constructor(text: string, id?: number, parent?: CommentDTO) {
    this.id = id;
    this.text = text;
    this.parent = parent;
  }
}

export class RateDTO {
  equipment: number;
  staff: number;
  hygiene: number;
  space: number;

  constructor(equipment: number, staff: number, hygiene: number, space: number) {
    this.equipment = equipment;
    this.staff = staff;
    this.hygiene = hygiene;
    this.space = space;
  }
}

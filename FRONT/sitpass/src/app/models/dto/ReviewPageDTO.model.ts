

export class ReviewPageDTO {
  id?: number;
  name: string;
  equipment: number;
  staff: number;
  hygiene: number;
  space: number;
  comment?: CommentForReviewDTO;
  createdAt: Date;
  exerciseCount: number;
  hidden: boolean;


  constructor(id: number, name: string, equipment: number, staff: number, hygiene: number, space: number, comment: CommentForReviewDTO, createdAt: Date, exerciseCount: number, hidden: boolean) {
    this.id = id;
    this.name = name;
    this.equipment = equipment;
    this.staff = staff;
    this.hygiene = hygiene;
    this.space = space;
    this.comment = comment;
    this.createdAt = createdAt;
    this.exerciseCount = exerciseCount;
    this.hidden = hidden;
  }
}

export class CommentForReviewDTO {
  id: number;
  comment: string;


  constructor(id: number, comment: string) {
    this.id = id;
    this.comment = comment;
  }
}

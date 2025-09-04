export class CommentToSendDTO {
  userId: number;
  comment: string;
  parentId: number;


  constructor(userId: number, comment: string, parentId: number) {
    this.userId = userId;
    this.comment = comment;
    this.parentId = parentId;
  }
}

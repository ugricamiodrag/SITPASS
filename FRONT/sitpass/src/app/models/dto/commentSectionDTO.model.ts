
export class CommentSectionDTO {
  id: number;
  username: string;
  comment: string;
  createdAt: Date;
  replies?: CommentSectionDTO[];


  constructor(id: number, username: string, comment: string, createdAt: Date, replies: CommentSectionDTO[]) {
    this.id = id;
    this.username = username;
    this.comment = comment;
    this.createdAt = createdAt;
    this.replies = replies;
  }
}

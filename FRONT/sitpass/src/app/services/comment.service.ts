import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CommentDTO} from "../models/dto/ReviewDTO.model";
import {CommentSectionDTO} from "../models/dto/commentSectionDTO.model";
import {ConfigService} from "./config.service";
import {CommentToSendDTO} from "../models/dto/commentDTO.model";

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private http: HttpClient, private config: ConfigService) { }

  getComments(id: number): Observable<CommentSectionDTO> {
    console.log("Hello from comment service")
    return this.http.get<CommentSectionDTO>(this.config.getCommentsUrl(id));
  }

  save(comment: CommentToSendDTO): Observable<CommentToSendDTO> {
    return this.http.post<CommentToSendDTO>(this.config.commentsUrl, comment);
  }

  getUserIdByCommentId(id: number): Observable<number> {
    return this.http.get<number>(this.config.getUserIdFromComment(id));
  }
}

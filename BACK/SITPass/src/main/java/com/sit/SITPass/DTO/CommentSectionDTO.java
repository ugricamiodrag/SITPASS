package com.sit.SITPass.DTO;

import com.sit.SITPass.model.Comment;
import com.sit.SITPass.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentSectionDTO {


    private Long id;
    private String username;
    private String comment;
    private LocalDateTime createdAt;
    private List<CommentSectionDTO> replies;

    public CommentSectionDTO() {
    }

    public CommentSectionDTO(Comment comment) {
        this.id = comment.getId();
        if (comment.getUser() != null && comment.getUser().getName() != null && comment.getUser().getSurname() != null) {
            this.username = comment.getUser().getName() + " " + comment.getUser().getSurname();
        } else {
            this.username = "Unknown user";
        }
        this.comment = comment.getText();
        this.createdAt = comment.getCreatedAt();
        this.replies = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<CommentSectionDTO> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentSectionDTO> replies) {
        this.replies = replies;
    }

    public static List<CommentSectionDTO> createFromComments(List<Comment> comments, CommentService commentService) {
        List<CommentSectionDTO> commentSectionDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            CommentSectionDTO dto = new CommentSectionDTO(comment);
            List<Comment> replies = commentService.findReplies(comment.getId());
            if (replies != null && !replies.isEmpty()) {
                dto.setReplies(createFromComments(replies, commentService));
            }
            commentSectionDTOs.add(dto);
        }
        return commentSectionDTOs;
    }
}

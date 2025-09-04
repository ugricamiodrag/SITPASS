package com.sit.SITPass.DTO;

public class CommentToSendDTO {
    private Long userId;
    private String comment;
    private Long parentId;

    public CommentToSendDTO() {
    }

    public CommentToSendDTO(Long userId, String comment, Long parentId) {
        this.userId = userId;
        this.comment = comment;
        this.parentId = parentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}

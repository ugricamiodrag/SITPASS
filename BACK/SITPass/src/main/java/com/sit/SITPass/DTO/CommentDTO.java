package com.sit.SITPass.DTO;

import jakarta.annotation.Nullable;

public class CommentDTO {

    @Nullable
    private Long id;
    private String text;
    @Nullable
    private CommentDTO parent;

    public CommentDTO() {}

    public CommentDTO(String text) {
        this.text = text;
    }

    public CommentDTO(@Nullable Long id, String text) {
        this.id = id;
        this.text = text;
    }

    public CommentDTO(@Nullable Long id, @Nullable CommentDTO parent) {
        this.id = id;
        this.parent = parent;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Nullable
    public CommentDTO getParent() {
        return parent;
    }

    public void setParent(@Nullable CommentDTO parent) {
        this.parent = parent;
    }
}

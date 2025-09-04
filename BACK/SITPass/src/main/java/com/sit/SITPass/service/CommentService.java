package com.sit.SITPass.service;

import com.sit.SITPass.model.Comment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CommentService {


    Optional<Comment> findById(Long id);
    Comment save(Comment comment);
    List<Comment> findReplies(Long id);
}

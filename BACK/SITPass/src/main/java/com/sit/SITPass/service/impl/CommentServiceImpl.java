package com.sit.SITPass.service.impl;

import com.sit.SITPass.model.Comment;
import com.sit.SITPass.repository.CommentRepository;
import com.sit.SITPass.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findReplies(Long id) {
        return commentRepository.findByReplies(id);
    }
}

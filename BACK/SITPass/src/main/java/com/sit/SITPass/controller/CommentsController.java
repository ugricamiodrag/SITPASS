package com.sit.SITPass.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sit.SITPass.DTO.CommentSectionDTO;
import com.sit.SITPass.DTO.CommentToSendDTO;
import com.sit.SITPass.model.Comment;
import com.sit.SITPass.model.User;
import com.sit.SITPass.service.CommentService;
import com.sit.SITPass.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/api/comments")
public class CommentsController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentSectionDTO> getComments(@PathVariable Long id) {
        System.out.println("Received request to get comments for ID: " + id);

        Optional<Comment> comment = commentService.findById(id);
        if (comment.isPresent()) {
            System.out.println("Comment found: " + comment.get().getId());

            List<Comment> comments = commentService.findReplies(id);
            System.out.println("Number of replies found: " + comments.size());

            List<CommentSectionDTO> sectionDTOS = CommentSectionDTO.createFromComments(comments, commentService);
            System.out.println("Replies converted to DTOs: " + sectionDTOS.size());

            CommentSectionDTO commentSectionDTO = new CommentSectionDTO(comment.get());
            commentSectionDTO.setReplies(sectionDTOS);

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule
                String json = objectMapper.writeValueAsString(commentSectionDTO);
                System.out.println("Final CommentSectionDTO JSON: " + json);
            } catch (Exception e) {
                System.err.println("Error converting CommentSectionDTO to JSON: " + e.getMessage());
            }


            return ResponseEntity.ok(commentSectionDTO);
        } else {
            System.out.println("Comment not found for ID: " + id);
            return ResponseEntity.notFound().build();
        }


    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<Long> getUserId(@PathVariable Long id) {
        Optional<Comment> comment = commentService.findById(id);
        if (comment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        System.out.println("Returning the id!");
        return ResponseEntity.ok(comment.get().getUser().getId());
    }

    @PostMapping
    public ResponseEntity<CommentToSendDTO> saveComment(@RequestBody CommentToSendDTO commentToSendDTO) {

        Optional<User> user = userService.getUserById(commentToSendDTO.getUserId());
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Optional<Comment> commentOptional = commentService.findById(commentToSendDTO.getParentId());
        if (commentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Comment comment = new Comment();
        comment.setText(commentToSendDTO.getComment());
        comment.setUser(user.get());
        comment.setReplyTo(commentOptional.get());
        comment.setCreatedAt(LocalDateTime.now());
        commentService.save(comment);

        return ResponseEntity.ok(commentToSendDTO);

    }
}

package com.sit.SITPass.repository;

import com.sit.SITPass.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Optional<Comment> findById(Long commentId);

    @Query("select c from Comment c where c.replyTo.id = ?1 order by c.id asc")
    List<Comment> findByReplies(Long userId);
}

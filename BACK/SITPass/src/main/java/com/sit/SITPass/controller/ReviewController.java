package com.sit.SITPass.controller;

import com.sit.SITPass.DTO.ReviewDTO;
import com.sit.SITPass.DTO.ReviewPageDTO;
import com.sit.SITPass.model.*;
import com.sit.SITPass.repository.UserRepository;
import com.sit.SITPass.service.*;
import com.sit.SITPass.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RateService rateService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private FacilityService facilityService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<?> review(@RequestBody ReviewDTO reviewDTO) {
        Review review = new Review();
        Rate rate = new Rate();
        Comment comment;

        System.out.println(reviewDTO.getUserId());

        Optional<User> user = userRepository.findById(reviewDTO.getUserId());
        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User userPresent = user.get();

        if (reviewDTO.getCommentDTO() != null) {
            comment = new Comment();
            comment.setText(reviewDTO.getCommentDTO().getText());
            comment.setUser(userPresent);

            if (reviewDTO.getCommentDTO().getParent() != null) {
                Optional<Comment> parentComment = commentService.findById(reviewDTO.getCommentDTO().getParent().getId());
                if (parentComment.isPresent()) {
                    comment.setReplyTo(parentComment.get());
                } else {
                    return ResponseEntity.badRequest().body("Parent comment not found");
                }
            }
            comment.setCreatedAt(LocalDateTime.now());
            Comment savedComment = commentService.save(comment);
            review.setComment(savedComment);
        }

        rate.setEquipment(reviewDTO.getRateDTO().getEquipment());
        rate.setStaff(reviewDTO.getRateDTO().getStaff());
        rate.setHygene(reviewDTO.getRateDTO().getHygiene());
        rate.setSpace(reviewDTO.getRateDTO().getSpace());
        Rate savedRate = rateService.save(rate);
        review.setRate(savedRate);

        int numberOfExercises = exerciseService.getNumberOfExercises(reviewDTO.getFacilityId(), reviewDTO.getUserId());
        review.setUser(userPresent);
        review.setExerciseCount(numberOfExercises);

        Optional<Facility> facilityOptional = facilityService.getFacility(reviewDTO.getFacilityId());
        if (!facilityOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Facility not found");
        }
        Facility facility = facilityOptional.get();

        Double averageScore = (rate.getEquipment() + rate.getSpace() + rate.getHygene() + rate.getStaff()) / 4.0;
        facilityService.updateAverageScore(facility.getId(), averageScore);

        review.setFacility(facility);
        review.setCreatedAt(LocalDateTime.now());
        review.setHidden(false);

        reviewService.saveReview(review);

        return ResponseEntity.ok(reviewDTO);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<List<ReviewPageDTO>> getAllReviews(@PathVariable Long id) {
        List<ReviewPageDTO> reviewPageDTOS = new ArrayList<>();
        List<Review> allReviews;
        if (userDetailsService.hasRole("ADMIN") || userDetailsService.hasRole("MANAGER")) {
            allReviews = reviewService.getReviews(id);
        } else {
            allReviews = reviewService.getActiveReviews(id);

        }

        for (Review review : allReviews) {
            reviewPageDTOS.add(reviewService.convertToReviewPageDTO(review));
            System.out.println("This is review: " + review.getId());
        }
        return ResponseEntity.ok(reviewPageDTOS);

    }

    @GetMapping(value = "/user/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<List<ReviewPageDTO>> getAllReviewsByUser(@PathVariable Long id) {
        List<ReviewPageDTO> reviewPageDTOS = new ArrayList<>();
        List<Review> allReviews = reviewService.getReviewsByUser(id);
        for (Review review : allReviews) {
            reviewPageDTOS.add(reviewService.convertToReviewPageDTO(review));
        }
        return ResponseEntity.ok(reviewPageDTOS);

    }

    @DeleteMapping(value = "/user/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        Optional<Review> review = reviewService.getReviewById(id);
        if (!review.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        reviewService.deleteReview(id);
        return ResponseEntity.ok(review.get().getId());

    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> hideReview(@PathVariable Long id, @RequestBody Map<String, Boolean> request) {
        Boolean hide = request.get("hide");
        Optional<Review> review = reviewService.getReviewById(id);
        if (!review.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        review.get().setHidden(hide);
        reviewService.saveReview(review.get());
        return ResponseEntity.ok(review.get().getId());
    }



}

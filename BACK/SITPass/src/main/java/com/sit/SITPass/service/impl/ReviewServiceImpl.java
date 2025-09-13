package com.sit.SITPass.service.impl;

import com.sit.SITPass.DTO.AnalyticsData;
import com.sit.SITPass.DTO.FacilityAverageRatingDTO;
import com.sit.SITPass.DTO.ReviewPageDTO;
import com.sit.SITPass.DTO.TimePeriodData;
import com.sit.SITPass.model.Facility;
import com.sit.SITPass.model.Review;
import com.sit.SITPass.repository.ReviewRepository;
import com.sit.SITPass.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviews(Long id) {
        return reviewRepository.findByFacilityId(id);
    }

    @Override
    public ReviewPageDTO convertToReviewPageDTO(Review review) {
        ReviewPageDTO result = new ReviewPageDTO();
        if (review.getId() != null) {
            result.setId(review.getId());
        }
        if (review.getUser().getName() == null && review.getUser().getSurname() == null) {
            result.setName("Unknown user");
        }
        else {
            result.setName(review.getUser().getName() + " " + review.getUser().getSurname());
        }

        result.setEquipment(review.getRate().getEquipment());
        result.setHygiene(review.getRate().getHygene());
        result.setStaff(review.getRate().getStaff());
        result.setSpace(review.getRate().getSpace());
        if (review.getComment() != null && review.getComment().getText() != null) {
            ReviewPageDTO.CommentForReviewDTO dto = result.new CommentForReviewDTO();
            dto.setId(review.getComment().getId());
            dto.setComment(review.getComment().getText());
            result.setComment(dto);
        }
        result.setHidden(review.getHidden());
        result.setCreatedAt(review.getCreatedAt());
        result.setExerciseCount(review.getExerciseCount());
        return result;
    }

    @Override
    public List<Review> getReviewsByUser(Long id) {
        return reviewRepository.findByUserId(id);
    }

    @Override
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.getReviewById(id);
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        Optional<Review> review = reviewRepository.getReviewById(id);
        if (review.isPresent()) {
            Facility facility = review.get().getFacility();
            double newRating = (facility.getTotalRating() * 2 - ((review.get().getRate().getSpace() + review.get().getRate().getStaff() + review.get().getRate().getHygene() + review.get().getRate().getEquipment()) / 4.0));
            facility.setTotalRating(newRating);
        }
        reviewRepository.deleteReviewById(id);

    }

    @Override
    public List<Review> getActiveReviews(Long id) {
        return reviewRepository.getReviewsByHiddenFalseAndFacilityId(id);
    }

    @Override
    public List<AnalyticsData> getAnalyticsData(Long facilityId, LocalDate from, LocalDate to) {
        return reviewRepository.getAnalyticsData(facilityId, from, to);
    }

    @Override
    public int getCountOfReviewsForFacility(Long facility) {
        return reviewRepository.countByFacilityId(facility);
    }

    @Override
    public FacilityAverageRatingDTO getFacilityAverageRating(Long id) {
        return reviewRepository.getAverageRatingsByFacility(id);
    }


}

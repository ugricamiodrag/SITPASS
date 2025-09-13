package com.sit.SITPass.service;

import com.sit.SITPass.DTO.AnalyticsData;
import com.sit.SITPass.DTO.FacilityAverageRatingDTO;
import com.sit.SITPass.DTO.ReviewPageDTO;
import com.sit.SITPass.DTO.TimePeriodData;
import com.sit.SITPass.model.Review;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public interface ReviewService {
    void saveReview(Review review);
    List<Review> getReviews(Long id);
    ReviewPageDTO convertToReviewPageDTO(Review review);
    List<Review> getReviewsByUser(Long id);
    Optional<Review> getReviewById(Long id);
    void deleteReview(Long id);
    List<Review> getActiveReviews(Long id);
    List<AnalyticsData> getAnalyticsData(Long facilityId, LocalDate from, LocalDate to);
    int getCountOfReviewsForFacility(Long facility);
    FacilityAverageRatingDTO getFacilityAverageRating(Long id);
}

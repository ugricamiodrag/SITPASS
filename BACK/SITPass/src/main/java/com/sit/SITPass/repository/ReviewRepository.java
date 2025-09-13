package com.sit.SITPass.repository;

import com.sit.SITPass.DTO.AnalyticsData;
import com.sit.SITPass.DTO.FacilityAverageRatingDTO;
import com.sit.SITPass.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByFacilityId(Long facilityId);
    List<Review> findByUserId(Long userId);
    Optional<Review> getReviewById(Long id);
    void deleteReviewById(Long id);

    @Query("select r from Review r where r.facility.id = ?1 and r.isHidden = false")
    List<Review> getReviewsByHiddenFalseAndFacilityId(Long id);

    @Query("select new com.sit.SITPass.DTO.AnalyticsData(r.createdAt, count(distinct r.user.id), count(r.id)) " +
            "from Review r " +
            "where r.facility.id = :facilityId and FUNCTION('DATE', r.createdAt) between :fromDate and :toDate " +
            "group by r.createdAt")
    List<AnalyticsData> getAnalyticsData(@Param("facilityId") Long facilityId,
                                         @Param("fromDate") LocalDate from,
                                         @Param("toDate") LocalDate to);

    int countByFacilityId(Long facilityId);

    @Query("""
        SELECT new com.sit.SITPass.DTO.FacilityAverageRatingDTO(
            r.facility.id,
            AVG(rt.equipment),
            AVG(rt.hygene),
            AVG(rt.space),
            AVG(rt.staff)
        )
        FROM Review r
        JOIN Rate rt ON r.rate.id = rt.id
        WHERE r.facility.id = :facilityId
        GROUP BY r.facility.id
    """)
    FacilityAverageRatingDTO getAverageRatingsByFacility(@Param("facilityId") Long facilityId);
}

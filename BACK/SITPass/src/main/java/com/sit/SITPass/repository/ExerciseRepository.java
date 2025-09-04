package com.sit.SITPass.repository;

import com.sit.SITPass.DTO.TimePeriodData;
import com.sit.SITPass.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {

    @Query("select count(e) from Exercise e where e.facility.id = ?1 and e.user.id = ?2 ")
    int countByFacilityIdAndUserId(Long FacilityId, Long UserId);

    @Query("select new com.sit.SITPass.DTO.TimePeriodData(e.from, count(e.user.id)) " +
            "from Exercise e " +
            "where e.facility.id = :facilityId " +
            "and FUNCTION('DATE', e.from) >= :fromDate " +
            "and FUNCTION('DATE', e.until) <= :toDate " +
            "group by e.from")
    List<TimePeriodData> getTimePeriodData(@Param("facilityId") Long facilityId,
                                           @Param("fromDate") LocalDate fromDate,
                                           @Param("toDate") LocalDate toDate);

}

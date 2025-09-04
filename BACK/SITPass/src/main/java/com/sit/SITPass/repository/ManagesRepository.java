package com.sit.SITPass.repository;

import com.sit.SITPass.model.Manages;
import com.sit.SITPass.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManagesRepository extends JpaRepository<Manages, Long> {
    List<Manages> findAllByUser(User user);

    int countByFacilityIdAndEndDateIsNull(Long facilityId);

    @Query("select m from Manages m where m.facility.id = ?1 and m.endDate is null")
    Optional<Manages> findByFacilityIdAndEndDateIsNull(Long facilityId);

    @Query("select m from Manages m where m.user.id = ?1 and m.facility.id = ?2")
    Optional<Manages> findByUserId(Long userId, Long facilityId);
}

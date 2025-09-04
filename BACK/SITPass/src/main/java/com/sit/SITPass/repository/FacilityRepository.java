package com.sit.SITPass.repository;

import com.sit.SITPass.model.Facility;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long>, JpaSpecificationExecutor<Facility> {
    Optional<Facility> findById(Long id);

    @Modifying
    @Transactional
    @Query("update Facility f set f.isActive = case when f.isActive = true then false else true end where f.id = :id")
    void hideById(Long id);

    @Query("SELECT f FROM Facility f WHERE f.isActive = true and f.city = :city")
    List<Facility> getFacilitiesByActiveTrue(String city);


    @Query("SELECT f FROM Facility f JOIN Manages m ON f.id = m.facility.id WHERE m.user.id = :id")
    List<Facility> findFacilitiesByManagerId(@Param("id") Long id);

    @Query("select f from Facility f where f.isActive = true order by f.totalRating desc")
    List<Facility> findAllByPopularity();

    @Query("select f from Facility f where f.id in (select e.facility.id from Exercise e where e.user.id = :id) and f.isActive = true")
    List<Facility> getVisitedFacilities(@Param("id")Long id);

    @Query("SELECT f FROM Facility f LEFT JOIN Exercise e ON f.id = e.facility.id AND e.user.id = :userId WHERE e.id IS NULL AND f.isActive = true")
    Page<Facility> getNewFacilities(@Param("userId") Long userId, Pageable pageable);







}

package com.sit.SITPass.repository;

import com.sit.SITPass.model.AccountRequest;
import com.sit.SITPass.model.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AccountRequestRepository  extends JpaRepository<AccountRequest, Long> {
    @Query("select count(a) > 0 from AccountRequest a where a.email = :email and a.requestStatus = 'ACCEPTED'")
    boolean existsByEmail(@Param("email") String email);


    Page<AccountRequest> findAccountRequestsByRequestStatusEquals(RequestStatus status, Pageable pageable);

    @Query("select a from AccountRequest a where a.email = :email and a.requestStatus = 'PENDING'")
    AccountRequest findByEmail(@Param("email") String email);
}
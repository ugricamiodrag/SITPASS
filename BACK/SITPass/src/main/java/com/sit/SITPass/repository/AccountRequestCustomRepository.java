package com.sit.SITPass.repository;

import com.sit.SITPass.model.AccountRequest;
import com.sit.SITPass.model.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface AccountRequestCustomRepository {
    Page<AccountRequest> findByRequestStatus(RequestStatus status, Pageable pageable);
}
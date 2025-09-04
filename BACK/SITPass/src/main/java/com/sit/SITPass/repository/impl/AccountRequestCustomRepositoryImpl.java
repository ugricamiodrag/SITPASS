package com.sit.SITPass.repository.impl;

import com.sit.SITPass.DTO.UserRequestDTO;
import com.sit.SITPass.model.AccountRequest;
import com.sit.SITPass.model.RequestStatus;
import com.sit.SITPass.repository.AccountRequestCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Repository
public class AccountRequestCustomRepositoryImpl implements AccountRequestCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Page<AccountRequest> findByRequestStatus(RequestStatus status, Pageable pageable) {
        String qlString = "SELECT ar FROM AccountRequest ar WHERE ar.requestStatus = :status";
        TypedQuery<AccountRequest> query = entityManager.createQuery(qlString, AccountRequest.class);
        query.setParameter("status", status);

        // Set pagination
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<AccountRequest> list = query.getResultList(); // Get result list directly

        if (list.isEmpty()) {
            System.out.println("The list is empty");
        } else {
            for (AccountRequest accountRequest : list) {
                System.out.println("Here is the request: " + accountRequest);
            }
        }

        // Get the total count
        TypedQuery<Long> countQuery = entityManager.createQuery(
                "SELECT COUNT(ar) FROM AccountRequest ar WHERE ar.requestStatus = :status", Long.class);
        countQuery.setParameter("status", status);
        long total = countQuery.getSingleResult();

        System.out.println("Total result: " + total);

        return new PageImpl<>(list, pageable, total);
    }
}
package com.sit.SITPass.service.impl;

import com.sit.SITPass.model.Rate;
import com.sit.SITPass.repository.RateRepository;
import com.sit.SITPass.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RateServiceImpl implements RateService {
    @Autowired
    private RateRepository rateRepository;

    @Override
    public Rate save(Rate rate) {
        return rateRepository.save(rate);
    }
}

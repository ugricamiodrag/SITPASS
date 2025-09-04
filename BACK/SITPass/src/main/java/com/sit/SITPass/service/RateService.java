package com.sit.SITPass.service;

import com.sit.SITPass.model.Rate;
import org.springframework.stereotype.Service;

@Service
public interface RateService {
    Rate save(Rate rate);

}

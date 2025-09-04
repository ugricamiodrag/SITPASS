package com.sit.SITPass.service;

import com.sit.SITPass.model.WorkDay;
import org.springframework.stereotype.Service;

@Service
public interface WorkDayService {

    void updateWorkDay(WorkDay workDay);
}

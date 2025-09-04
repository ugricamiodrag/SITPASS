package com.sit.SITPass.service.impl;

import com.sit.SITPass.model.WorkDay;
import com.sit.SITPass.repository.WorkDayRepository;
import com.sit.SITPass.service.WorkDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkDayServiceImpl implements WorkDayService {

    @Autowired
    private WorkDayRepository workDayRepository;

    @Override
    public void updateWorkDay(WorkDay workDay) {
        workDayRepository.save(workDay);
    }
}

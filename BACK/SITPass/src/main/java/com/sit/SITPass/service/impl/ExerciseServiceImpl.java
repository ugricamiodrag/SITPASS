package com.sit.SITPass.service.impl;

import com.sit.SITPass.DTO.TimePeriodData;
import com.sit.SITPass.model.Exercise;
import com.sit.SITPass.repository.ExerciseRepository;
import com.sit.SITPass.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Override
    public void saveExercise(Exercise exercise) {
        exerciseRepository.save(exercise);
    }

    @Override
    public int getNumberOfExercises(Long facilityId, Long userId) {
        return exerciseRepository.countByFacilityIdAndUserId(facilityId, userId);
    }

    @Override
    public List<TimePeriodData> getTimePeriodData(Long facilityId, LocalDate from, LocalDate to) {
        return exerciseRepository.getTimePeriodData(facilityId, from, to);
    }
}

package com.sit.SITPass.service;

import com.sit.SITPass.DTO.TimePeriodData;
import com.sit.SITPass.model.Exercise;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface ExerciseService {

    void saveExercise(Exercise exercise);

    int getNumberOfExercises(Long facilityId, Long userId);

    List<TimePeriodData> getTimePeriodData(Long facilityId, LocalDate from, LocalDate to);

}

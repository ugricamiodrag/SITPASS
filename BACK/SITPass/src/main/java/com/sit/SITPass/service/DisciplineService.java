package com.sit.SITPass.service;

import com.sit.SITPass.model.Discipline;
import org.springframework.stereotype.Service;

@Service
public interface DisciplineService {
    void updateDiscipline(Discipline discipline);
}

package com.sit.SITPass.service.impl;

import com.sit.SITPass.model.Discipline;
import com.sit.SITPass.repository.DisciplineRepository;
import com.sit.SITPass.service.DisciplineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DisciplineServiceImpl implements DisciplineService {

    @Autowired
    private DisciplineRepository disciplineRepository;


    @Override
    public void updateDiscipline(Discipline discipline) {
        disciplineRepository.save(discipline);
    }
}

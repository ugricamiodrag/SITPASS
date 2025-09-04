package com.sit.SITPass.specifications;

import com.sit.SITPass.DTO.FacilitySearchCriteria;
import com.sit.SITPass.DTO.WorkDayDTO;
import com.sit.SITPass.model.Discipline;
import com.sit.SITPass.model.Facility;
import com.sit.SITPass.model.WorkDay;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FacilitySpecification implements Specification<Facility> {

    private FacilitySearchCriteria criteria;

    public FacilitySpecification(FacilitySearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Facility> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        // Check only active facilities
        predicates.add(criteriaBuilder.isTrue(root.get("isActive")));

        if (criteria.getCities() != null && !criteria.getCities().isEmpty()) {
            predicates.add(root.get("city").in(criteria.getCities()));
            System.out.println("Cities in criteria: " + criteria.getCities());
        }

        if (criteria.getDisciplines() != null && !criteria.getDisciplines().isEmpty()) {
            Join<Facility, Discipline> disciplineJoin = root.join("disciplines");
            predicates.add(disciplineJoin.get("name").in(criteria.getDisciplines()));
        }

        if (criteria.getMinGrade() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), criteria.getMinGrade()));
        }

        if (criteria.getMaxGrade() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("rating"), criteria.getMaxGrade()));
        }

        if (criteria.getWorkDaySearchCriteriaList() != null && !criteria.getWorkDaySearchCriteriaList().isEmpty()) {
            List<Predicate> workDayPredicates = new ArrayList<>();

            for (WorkDayDTO workDayCriteria : criteria.getWorkDaySearchCriteriaList()) {
                Join<Facility, WorkDay> workDayJoin = root.join("workDays", JoinType.LEFT);

                Predicate dayPredicate = null;
                System.out.println("workDayCriteria.getDayOfWeek()" + workDayCriteria.getDayOfWeek());
                if (workDayCriteria.getDayOfWeek() != null) {
                    dayPredicate = criteriaBuilder.equal(workDayJoin.get("day"), workDayCriteria.getDayOfWeek());
                }

                Predicate timePredicate = null;
                if (workDayCriteria.getStartTime() != null && workDayCriteria.getEndTime() != null) {
                    timePredicate = criteriaBuilder.and(
                            criteriaBuilder.lessThanOrEqualTo(workDayJoin.get("from"), workDayCriteria.getEndTime()),
                            criteriaBuilder.greaterThanOrEqualTo(workDayJoin.get("until"), workDayCriteria.getStartTime())
                    );
                }

                if (dayPredicate != null && timePredicate != null) {
                    workDayPredicates.add(criteriaBuilder.and(dayPredicate, timePredicate));
                }
            }

            if (!workDayPredicates.isEmpty()) {
                Predicate workDayPredicate = criteriaBuilder.or(workDayPredicates.toArray(new Predicate[0]));
                predicates.add(workDayPredicate);
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}

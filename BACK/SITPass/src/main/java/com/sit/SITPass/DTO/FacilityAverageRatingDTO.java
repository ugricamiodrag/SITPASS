package com.sit.SITPass.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FacilityAverageRatingDTO {
    private Long facilityId;
    private Double avgEquipment;
    private Double avgHygene;
    private Double avgSpace;
    private Double avgStaff;

    public FacilityAverageRatingDTO(Long facilityId, Double avgEquipment, Double avgHygene, Double avgSpace, Double avgStaff) {
        this.facilityId = facilityId;
        this.avgEquipment = avgEquipment;
        this.avgHygene = avgHygene;
        this.avgSpace = avgSpace;
        this.avgStaff = avgStaff;
    }

}

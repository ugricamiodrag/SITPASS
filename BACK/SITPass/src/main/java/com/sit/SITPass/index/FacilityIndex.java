package com.sit.SITPass.index;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "facility_index")
@Setting(settingPath = "serbian-analyzer-config.json")
public class FacilityIndex {

    @Id
    private String id;

    @Field(type = FieldType.Text, store = true, name = "name")
    private String name;

    @Field(type = FieldType.Text, store = true ,analyzer = "english", name = "description_en")
    private String descriptionEn;

    @Field(type = FieldType.Text, store = true, analyzer = "serbian", name = "description_sr")
    private String descriptionSr;

    @Field(type = FieldType.Text,  store = true, analyzer = "english", name = "fileDescription_en")
    private String fileDescriptionEn;

    @Field(type = FieldType.Text, store = true, analyzer = "serbian", name = "fileDescription_sr")
    private String fileDescriptionSr;

    @Field(type = FieldType.Text, store = true, name = "server_filename", index = false)
    private String serverFilename;

    @Field(type = FieldType.Integer, store = true, name = "reviewCount")
    private Integer reviewCount;

    @Field(type = FieldType.Double, store = true, name = "avgEquipmentGrade")
    private Double avgEquipmentGrade;

    @Field(type = FieldType.Double, store = true, name = "avgStaffGrade")
    private Double avgStaffGrade;

    @Field(type = FieldType.Double, store = true, name = "avgHygieneGrade")
    private Double avgHygieneGrade;

    @Field(type = FieldType.Double, store = true, name = "avgSpaceGrade")
    private Double avgSpaceGrade;
}

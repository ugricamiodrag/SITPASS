package com.sit.SITPass.controller;

import com.sit.SITPass.DTO.ExerciseDTO;
import com.sit.SITPass.DTO.ExerciseToSendDTO;
import com.sit.SITPass.model.Exercise;
import com.sit.SITPass.model.Facility;
import com.sit.SITPass.model.User;
import com.sit.SITPass.service.ExerciseService;
import com.sit.SITPass.service.FacilityService;
import com.sit.SITPass.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/api/exercises")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private FacilityService facilityService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createReservation(@RequestBody ExerciseToSendDTO exercise) {

        Optional<Facility> facility = facilityService.getFacility(exercise.getFacilityId());
        Optional<User> user = userService.getUserById(exercise.getUserId());
        if (facility.isPresent() && user.isPresent()) {
            LocalDateTime from = exercise.getFrom();
            LocalDateTime until = exercise.getUntil();
            ZonedDateTime utcFrom = from.atZone(ZoneId.of("UTC"));
            ZonedDateTime utcUntil = until.atZone(ZoneId.of("UTC"));

            ZonedDateTime serverZoneFrom = utcFrom.withZoneSameInstant(ZoneId.systemDefault());
            ZonedDateTime serverZoneUntil = utcUntil.withZoneSameInstant(ZoneId.systemDefault());

            System.out.println("This is the exercise dates: " + serverZoneFrom + " " + serverZoneUntil);

            Exercise exerciseToSave = new Exercise();
            exerciseToSave.setUser(user.get());
            exerciseToSave.setFacility(facility.get());

            exerciseToSave.setFrom(serverZoneFrom.toLocalDateTime());
            exerciseToSave.setUntil(serverZoneUntil.toLocalDateTime());

            exerciseService.saveExercise(exerciseToSave);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();



    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @PostMapping(value = "/number")
    public int getCountOfExercises(@RequestBody ExerciseDTO request) {
        System.out.println("Facility id: " + request.getId() + "\n" + exerciseService.getNumberOfExercises(request.getId(), request.getUserId()) + "\n" + request.getUserId());
        return exerciseService.getNumberOfExercises(request.getId(), request.getUserId());
    }
}

package com.sit.SITPass.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sit.SITPass.DTO.FacilityDTO;
import com.sit.SITPass.DTO.FacilitySearchCriteria;
import com.sit.SITPass.DTO.ManagesDTO;
import com.sit.SITPass.model.*;
import com.sit.SITPass.repository.FacilityRepository;
import com.sit.SITPass.repository.ManagesRepository;
import com.sit.SITPass.repository.UserRepository;
import com.sit.SITPass.service.*;
import com.sit.SITPass.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/facilities")
public class FacilityController {

    @Value("${upload.dir}")
    private String uploadDir;

    private final ObjectMapper objectMapper;
    @Autowired
    private ManagesRepository managesRepository;
    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public FacilityController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    private ManagesService managesService;

    @Autowired
    private FacilityService facilityService;

    @Autowired
    private WorkDayService workDayService;

    @Autowired
    private DisciplineService disciplineService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private FileService fileService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<FacilityDTO> createFacility(@RequestParam("name") String name,
                                                      @RequestParam("description") String description,
                                                      @RequestParam("address") String address,
                                                      @RequestParam("city") String city,
                                                      @RequestParam(value = "workDays", required = false) String workDaysJson,
                                                      @RequestParam(value = "disciplines", required = false) String disciplinesJson,
                                                      @RequestParam(value = "photos", required = false) MultipartFile[] photos) throws IOException {



        Set<Image> allPhotos = new HashSet<>();
        if (photos != null) {

            for (MultipartFile photo : photos) {
                if (!photo.isEmpty()) {
                    byte[] bytes = photo.getBytes();
                    String fileName = photo.getOriginalFilename();
                    Path path = Paths.get(fileName);
                    Files.write(path, bytes);

                    Image image = new Image();
                    image.setPath(path.toString());
                    fileService.store(photo, fileName);
                    allPhotos.add(image);
                }
            }
        }


        Facility facility = new Facility();
        facility.setName(name);
        facility.setDescription(description);
        facility.setAddress(address);
        facility.setCity(city);
        Set<WorkDay> workDays = new HashSet<>();
        if (workDaysJson != null) {
            workDays = objectMapper.readValue(workDaysJson, objectMapper.getTypeFactory().constructCollectionType(Set.class, WorkDay.class));
            facility.setWorkDays(workDays);
        }
        else {
            facility.setWorkDays(new HashSet<>());
        }

        Set<Discipline> disciplines = new HashSet<>();
        if (disciplinesJson != null) {
            disciplines = objectMapper.readValue(disciplinesJson, objectMapper.getTypeFactory().constructCollectionType(Set.class, Discipline.class));
            facility.setDisciplines(disciplines);
            facility.setWorkDays(new HashSet<>());
        }




        facility.setImages(allPhotos);
        Facility savedFacility = facilityService.createFacility(facility);
        if (!workDays.isEmpty()) {
            for (WorkDay workDay : workDays) {
                workDay.setFacility(savedFacility);
                workDayService.updateWorkDay(workDay);
            }

        }

        if (!disciplines.isEmpty()) {
            for (Discipline discipline : disciplines) {
                discipline.setFacility(savedFacility);
                disciplineService.updateDiscipline(discipline);
            }
        }
        for (Image image: allPhotos){
            image.setFacility(savedFacility);
            imageService.updateImage(image);
        }


        FacilityDTO facilityDTO = convertToDto(savedFacility);
        return ResponseEntity.ok(facilityDTO);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<FacilityDTO> updateFacility(@PathVariable Long id,
                                                      @RequestParam("name") String name,
                                                      @RequestParam("description") String description,
                                                      @RequestParam("address") String address,
                                                      @RequestParam("city") String city,
                                                      @RequestParam(value = "workDays", required = false) String workDaysJson,
                                                      @RequestParam(value = "disciplines", required = false) String disciplinesJson,
                                                      @RequestParam(value = "photos", required = false) MultipartFile[] photos,
                                                      @RequestParam(value = "photosToRemove", required = false) String photosToRemoveJson) throws IOException {
        Optional<Facility> optionalFacility = facilityService.getFacility(id);
        if (optionalFacility.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Facility existingFacility = optionalFacility.get();
        existingFacility.setName(name);
        existingFacility.setDescription(description);
        existingFacility.setAddress(address);
        existingFacility.setCity(city);


        if (workDaysJson != null) {
            Set<WorkDay> workDays = objectMapper.readValue(workDaysJson, objectMapper.getTypeFactory().constructCollectionType(Set.class, WorkDay.class));
            for (WorkDay workDay : workDays) {
                workDay.setFacility(existingFacility);
                workDayService.updateWorkDay(workDay);
            }
            existingFacility.setWorkDays(workDays);
        }


        if (disciplinesJson != null) {
            Set<Discipline> disciplines = objectMapper.readValue(disciplinesJson, objectMapper.getTypeFactory().constructCollectionType(Set.class, Discipline.class));
            for (Discipline discipline : disciplines) {
                discipline.setFacility(existingFacility);
                disciplineService.updateDiscipline(discipline);
            }
            existingFacility.setDisciplines(disciplines);
        }



        if (photos != null) {
            Set<Image> newImages = new HashSet<>();
            for (MultipartFile photo : photos) {
                if (!photo.isEmpty()) {
                    byte[] bytes = photo.getBytes();
                    String fileName = photo.getOriginalFilename();
                    Path path = Paths.get(fileName);
                    Files.write(path, bytes);
                    System.out.println("Received file: " + fileName + " | Size: " + photo.getSize());
                    System.out.println("Saving file to path: " + path.toAbsolutePath());
                    Image image = new Image();
                    image.setPath(path.toString());
                    image.setFacility(existingFacility);
                    fileService.store(photo, fileName);
                    imageService.updateImage(image);
                    newImages.add(image);
                    System.out.println("File saved successfully: " + Files.exists(path));
                }
            }
            existingFacility.setImages(newImages);
        }

        Facility updatedFacility = facilityService.updateFacility(existingFacility);
        if (photosToRemoveJson != null) {
            String[] photosToRemove = objectMapper.readValue(photosToRemoveJson, String[].class);
            for (String photoPath : photosToRemove) {
                System.out.println("Deleting " + photoPath);
                Path path = Paths.get(photoPath);
                Files.deleteIfExists(path);
                fileService.delete(photoPath);
                imageService.deleteImageByPath(photoPath);
            }
        }
        FacilityDTO facilityDTO = convertToDto(updatedFacility);

        return ResponseEntity.ok(facilityDTO);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacility(@PathVariable Long id) {
        facilityService.deleteFacility(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacilityDTO> getFacility(@PathVariable Long id) {
        Optional<Facility> facility = facilityService.getFacility(id);
        if (facility.isPresent()) {
            Facility fac = facility.get();
            for (Image image : fac.getImages()) {
                System.out.println("Returning image path: " + image.getPath());
                File f = new File(image.getPath());
                System.out.println("File exists? " + f.exists() + " | Size: " + f.length());
            }
            return ResponseEntity.ok(convertToDto(fac));
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping
    public ResponseEntity<List<FacilityDTO>> getAllFacilities() {
        List<Facility> facilities;
        String email = userDetailsService.getCurrentUserEmail();
        Optional<User> user = userService.getUserByEmail(email);


        if (userDetailsService.hasRole("ADMIN")) {
            facilities = facilityService.getAllFacilities();
        }
        else if (userDetailsService.hasRole("MANAGER")) {
            facilities = facilityService.getFacilitiesByManagerId(user.get().getId());
        }

        else {
            if (user.get() != null && user.get().getCity() != null) {
                facilities = facilityService.getActiveFacilities(user.get().getCity());
            }
            else {
                facilities = facilityService.getActiveFacilities("");
            }

        }

        for (Facility facility : facilities) {
            System.out.println("Facility with rating: " + facility.getTotalRating());
            for (Image image : facility.getImages()) {
                System.out.println("Image: " + image.getPath());
            }
        }


        List<FacilityDTO> facilityDTOs = new ArrayList<>();
        for (Facility facility : facilities) {
            facilityDTOs.add(convertToDto(facility));
        }

        return ResponseEntity.ok(facilityDTOs);
    }

    @PutMapping(value = "/hide/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> hideFacility(@PathVariable Long id) {
        facilityService.hideById(id);
        return ResponseEntity.noContent().build();
    }

    private FacilityDTO convertToDto(Facility facility) {
        FacilityDTO facilityDTO = new FacilityDTO();
        facilityDTO.setId(facility.getId());
        facilityDTO.setName(facility.getName());
        facilityDTO.setDescription(facility.getDescription());
        facilityDTO.setAddress(facility.getAddress());
        facilityDTO.setTotalRating(facility.getTotalRating());
        facilityDTO.setCity(facility.getCity());
        facilityDTO.setWorkDays(facility.getWorkDays());
        facilityDTO.setDisciplines(facility.getDisciplines());
        facilityDTO.setActive(facility.isActive());
        System.out.println("FacilityDTO: " + facilityDTO.isActive());

        Set<String> imagePaths = facility.getImages().stream()
                .map(Image::getPath)
                .collect(Collectors.toSet());
        facilityDTO.setImages(imagePaths);
        return facilityDTO;
    }



    @PostMapping(value = "/manager/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Manages> setManages(@PathVariable Long id, @RequestBody ManagesDTO managesDTO) {
        Optional<Facility> facility = facilityService.getFacility(id);
        if (facility.isEmpty()) {
           return ResponseEntity.notFound().build();
        }
        else {
            Manages manages = new Manages();
            Optional<User> user = userRepository.findByEmail(managesDTO.getUser().getEmail());
            if (user.isPresent()) {
                manages.setUser(user.get());
                System.out.println("User: " + manages.getUser().getId());
            }
            manages.setFacility(facility.get());
            manages.setStartDate(managesDTO.getStartDate());
            Manages saved = managesRepository.save(manages);
            facility.get().setActive(true);
            facility.get().setManages(saved);
            facilityRepository.save(facility.get());
            return ResponseEntity.ok(saved);
        }
    }

    @PutMapping(value = "/manager/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Manages> removeManages(@PathVariable Long id, @RequestBody Manages manages) {
        Optional<Facility> facility = facilityService.getFacility(id);
        System.out.println("Facility: " + facility.get().getName());
        if (facility.isPresent()) {
            Optional<User> user = userRepository.findByEmail(manages.getUser().getEmail());

            if (user.isPresent()) {
                System.out.println("User: " + user.get().getId());
                Optional<Manages> manageToRemove = managesService.getManagerByUserId(user.get().getId(), facility.get().getId());
                System.out.println("ManageToRemove: " + manageToRemove);
                if (manageToRemove.isPresent()) {
                    manageToRemove.get().setEndDate(LocalDate.now());
                    Facility facility1 = manageToRemove.get().getFacility();
                    facility1.setActive(false);
                    facility1.setManages(null);
                    managesRepository.save(manageToRemove.get());
                    facilityRepository.save(facility1);
                    return ResponseEntity.ok(manageToRemove.get());
            }

            }

        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/search")
    public ResponseEntity<List<FacilityDTO>> searchFacility(@RequestBody FacilitySearchCriteria facilitySearchCriteria) {
        List<Facility> facilities = facilityService.getFacilitiesByCriteria(facilitySearchCriteria);
        System.out.println("Facilites exist? :" + facilities);
        List<FacilityDTO> facilityDTOs = new ArrayList<>();
        for (Facility facility : facilities) {
            facilityDTOs.add(convertToDto(facility));
        }
        return ResponseEntity.ok(facilityDTOs);
    }

    @GetMapping(value = "/popular")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<List<FacilityDTO>> getPopularFacilities() {
        List<FacilityDTO> dtos = new ArrayList<>();
        for (Facility facility: facilityService.getPopularFacilities()) {
            dtos.add(convertToDto(facility));
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping(value = "/visited")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<List<FacilityDTO>> getVisitedFacilities() {
        List<FacilityDTO> dtos = new ArrayList<>();
        String email = userDetailsService.getCurrentUserEmail();
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isPresent()) {
            for (Facility facility: facilityService.getVisitedFacilities(user.get().getId())) {
                dtos.add(convertToDto(facility));
            }
            return ResponseEntity.ok(dtos);
        }

        return ResponseEntity.notFound().build();

    }

    @PostMapping(value = "/unvisited")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Page<FacilityDTO>> getUnvisitedFacilities(@RequestBody Map<String, Integer> requestBody) {
        System.out.println("Hello from unvisitedFacilities");
        int page = requestBody.getOrDefault("page", 0);
        int pageSize = requestBody.getOrDefault("pageSize", 10);
        List<FacilityDTO> dtos = new ArrayList<>();
        String email = userDetailsService.getCurrentUserEmail();
        Optional<User> user = userService.getUserByEmail(email);

        if (user.isPresent()) {
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<Facility> facilityPage = facilityService.getNewFacilities(user.get().getId(), pageable);
            Page<FacilityDTO> facilityDTOS = facilityPage.map(this::convertToDto);

            return ResponseEntity.ok(facilityDTOS);
        }

        return ResponseEntity.notFound().build();

    }


    @GetMapping(value = "/managed")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    public ResponseEntity<List<FacilityDTO>> getManagedFacilities() {
        List<FacilityDTO> dtos = new ArrayList<>();
        String email = userDetailsService.getCurrentUserEmail();
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isPresent()) {
            List<Facility> facilities = facilityService.getFacilitiesByManagerId(user.get().getId());
            for (Facility facility : facilities) {
                dtos.add(convertToDto(facility));
            }
            return ResponseEntity.ok(dtos);
        }

        return ResponseEntity.notFound().build();
    }




}

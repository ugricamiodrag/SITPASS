package com.sit.SITPass.controller;

import com.sit.SITPass.DTO.*;
import com.sit.SITPass.model.AccountRequest;
import com.sit.SITPass.model.Image;
import com.sit.SITPass.model.RequestStatus;
import com.sit.SITPass.model.User;
import com.sit.SITPass.repository.UserRepository;
import com.sit.SITPass.security.TokenUtils;
import com.sit.SITPass.service.AccountRequestService;
import com.sit.SITPass.service.ImageService;
import com.sit.SITPass.service.UserService;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private AccountRequestService accountRequestService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenUtils tokenUtils;
    @Qualifier("userDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${upload.dir}")
    private String uploadDir;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageService imageService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    public UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/check-email")
    public ResponseEntity<?> checkEmailExists(@RequestParam String email) {
        boolean exists = accountRequestService.emailExists(email);
        return ResponseEntity.ok().body(Map.of("exists", exists));
    }


    @PostMapping(value = "register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> register(@RequestBody UserRequestDTO userRequestDTO) {
        if (!accountRequestService.emailExists(userRequestDTO.getEmail())) {
            try {
                AccountRequest request = new AccountRequest();
                request.setEmail(userRequestDTO.getEmail());
                request.setAddress(userRequestDTO.getAddress());
                request.setRequestStatus(RequestStatus.PENDING);
                request.setCreatedAt(userRequestDTO.getCreatedDate());

                accountRequestService.createRequest(request);

                Map<String, String> response = new HashMap<>();
                response.put("message", "Registration successful");
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } catch (Exception e) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Registration failed");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "The email already exists");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @PostMapping(value="/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenDTO> login(@RequestBody UserDTO userDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword());

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.getEmail());
            String tokenValue = this.tokenUtils.generateToken(userDetails);

            TokenDTO token = new TokenDTO();
            token.setToken(tokenValue);

            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping(
            value = "/logOut",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> logoutUser() throws BadRequestException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)){
            SecurityContextHolder.clearContext();

            return new ResponseEntity<>("You successfully logged out!", HttpStatus.OK);
        } else {
            throw new BadRequestException("WineUser is not authenticated!");
        }

    }


    @PostMapping(value="/change-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeDTO request) throws MessagingException {
        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();
        String repeatNewPassword = request.getRepeatPassword();

        if (!newPassword.equals(repeatNewPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Password don't match\"}");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalEmail = authentication.getName();

        Optional<User> userOptional = userRepository.findByEmail(currentPrincipalEmail);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"User not found\"}");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Old password is incorrect\"}");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
        userService.sendEmailAboutPassword(user);


        return ResponseEntity.ok("{\"message\": \"You have successfully changed the password\"}");
    }


    @PostMapping(value = "/email", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserByEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfileDTO> updateUser(@PathVariable("id") Long id, @RequestBody UserProfileDTO request) {
        System.out.println(request);
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEmail(request.getEmail());
            user.setAddress(request.getAddress());
            user.setName(request.getName());
            user.setSurname(request.getSurname());
            user.setCity(request.getCity());
            user.setBirthday(request.getBirthday());
            user.setZipCode(request.getZipCode());
            user.setPhoneNumber(request.getPhoneNumber());

            userRepository.save(user);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @PutMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateImage(@PathVariable("id") Long id, @RequestPart("photo") MultipartFile photo) throws IOException {
        byte[] bytes = photo.getBytes();
        String fileName = photo.getOriginalFilename();
        Path path = Paths.get(uploadDir + File.separator + fileName);
        Files.write(path, bytes);

        Image image = new Image();
        image.setPath(path.toString());
        Image savedImage = imageService.saveImage(image);
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Image image1 = user.getImage();
            if (image1 != null) {
                imageService.deleteImageByPath(image1.getPath());
            }

            user.setImage(savedImage);
            userRepository.save(user);
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserToManageDTO>> getAllUsers(@RequestBody String dummy) {
        List<UserToManageDTO> users = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            users.add(new UserToManageDTO(user));
        }
        return ResponseEntity.ok(users);
    }




}

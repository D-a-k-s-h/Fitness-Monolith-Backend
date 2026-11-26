package com.project.fitness.controller;

import com.project.fitness.dto.LoginRequest;
import com.project.fitness.dto.LoginResponse;
import com.project.fitness.dto.RegisterRequest;
import com.project.fitness.dto.UpdateProfileRequest;
import com.project.fitness.modal.User;
import com.project.fitness.service.UserService;
import com.project.fitness.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    //@Autowired
    //AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return userService.register(registerRequest);
    }

    @PostMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest request) {
        return userService.updateProfile(request);
    }

    @DeleteMapping("/deleteProfile/{userId}")
    public ResponseEntity<?> deleteProfile(@PathVariable(name = "userId") String userId) {
        return userService.deleteProfile(userId);
    }

    @GetMapping("/fetchProfile/{userId}")
    public ResponseEntity<?> fetchProfile(@PathVariable(name = "userId") String userId) {
        return userService.fetchProfile(userId);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        //Authentication authentication;
        try {
            User user = userService.authenticate(loginRequest);

            String token = jwtUtil.generateToken(user.getId(), user.getRole().name());

            return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(token, userService.mapToResponse(user), "Authenticated!"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body(e);
        }

        //SecurityContextHolder.getContext().setAuthentication(authentication);
        //UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        //This will return jwtToken;
        //return jwtUtil.generateToken(Objects.requireNonNull(userDetails));
    }
}
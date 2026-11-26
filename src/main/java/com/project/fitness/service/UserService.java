package com.project.fitness.service;

import com.project.fitness.dto.LoginRequest;
import com.project.fitness.dto.RegisterRequest;
import com.project.fitness.dto.UpdateProfileRequest;
import com.project.fitness.dto.UserResponse;
import com.project.fitness.modal.User;
import com.project.fitness.modal.UserRole;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> register(RegisterRequest request) {
        /*User user = new User(
                null,
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                Instant.parse("2026-09-23T18:04:21.573+05:30")
                        .atZone(ZoneOffset.UTC)
                        .toLocalDateTime(),
                Instant.parse("2026-09-23T18:04:21.573+05:30")
                        .atZone(ZoneOffset.UTC)
                        .toLocalDateTime(),
                List.of(),
                List.of()
        );*/

        try {
            User user = User.builder()
                    .email(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .age(request.getAge())
                    .gender(request.getGender())
                    .height(request.getHeight())
                    .weight(request.getWeight())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole() != null ? request.getRole() : UserRole.USER)
                    .build();

            User savedUser = userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(savedUser));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e);
        }
    }

    public UserResponse mapToResponse(User savedUser) {
        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getPassword(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt(),
                savedUser.getRole(),
                savedUser.getAge() != null ? savedUser.getAge() : 0,
                savedUser.getGender(),
                savedUser.getHeight() != null ? savedUser.getHeight() : 0,
                savedUser.getWeight() != null ? savedUser.getWeight() : 0
        );
    }

    public User authenticate(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user == null) {
            throw new RuntimeException("Invalid Credentials.");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Credentials.");
        }

        return user;
    }

    public ResponseEntity<?> updateProfile(UpdateProfileRequest request) {
        try {
            User user = userRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("No user found with id: " + request.getId()));

            user.setAge(request.getAge());
            user.setGender(request.getGender());
            user.setHeight(request.getHeight());
            user.setWeight(request.getWeight());

            userRepository.save(user);

            return ResponseEntity.status(200).body("Profile updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e);
        }
    }

    public ResponseEntity<?> deleteProfile(String userId) {
        try {
            userRepository.findById(userId).orElseThrow(() -> new RuntimeException("No user found with id: " + userId));

            userRepository.deleteById(userId);

            return ResponseEntity.status(200).body("Account deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e);
        }
    }

    public ResponseEntity<?> fetchProfile(String userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("No user found with id: " + userId));
            return ResponseEntity.ok().body(mapToResponse(user));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e);
        }
    }
}

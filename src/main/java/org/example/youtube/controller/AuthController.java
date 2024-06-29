package org.example.youtube.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.youtube.Service.AuthService;
import org.example.youtube.dto.auth.LoginDTO;
import org.example.youtube.dto.auth.RegistrationDTO;
import org.example.youtube.dto.profile.ProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
//@Tag(name = "Auth Controller", description = "Api list for authorization, registration and other ... ")
public class AuthController {

    @Autowired
    private AuthService authService;


    @GetMapping("/registrationByEmail/resend/{email}")
    public ResponseEntity<String> registrationResendByEmail(@PathVariable("email") String email) {
        String body = authService.registrationResendByEmail(email);

        return ResponseEntity.ok().body(body);
    }


    @PostMapping("/registrationByEmail")
    public ResponseEntity<String> registrationByEmail(@Valid @RequestBody RegistrationDTO dto) {
        String body = authService.registrationByEmail(dto);
        return ResponseEntity.ok().body(body);
    }

    @GetMapping("/verificationByEmail/{userId}")
    public ResponseEntity<String> verificationByEmail(@PathVariable("userId") Integer userId) {
        String body = authService.authorizationVerificationByEmail(userId);
        return ResponseEntity.ok().body(body);
    }

    @PostMapping("/login")
    public ResponseEntity<ProfileDTO> registrationByEmail(@Valid @RequestBody LoginDTO dto) {
        ProfileDTO body = authService.login(dto);
        return ResponseEntity.ok().body(body);
    }


}

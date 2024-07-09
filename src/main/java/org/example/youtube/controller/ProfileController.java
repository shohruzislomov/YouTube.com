package org.example.youtube.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.youtube.Service.ProfileService;
import org.example.youtube.dto.JwtDTO;
import org.example.youtube.dto.profile.ProfileCreateDTO;
import org.example.youtube.dto.profile.ProfileDTO;
import org.example.youtube.dto.profile.ProfileUpdateDTO;
import org.example.youtube.enums.ProfileRole;
import org.example.youtube.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfileDTO> create(@Valid @RequestBody ProfileCreateDTO profile) {
        ProfileDTO response = profileService.create(profile);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/current")
    public ResponseEntity<Boolean> updateUser(@RequestBody ProfileUpdateDTO profile) {
        profileService.update(profile);
        return ResponseEntity.ok().body(true);
    }

   /* @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/current/update-attach")
    public ResponseEntity<ProfileDTO> updateAttach(@RequestParam("image") MultipartFile file) {
        return ResponseEntity.ok().body(profileService.updateAttach(file));
    }*/


    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/current/update-email/{email}")
    public ResponseEntity<String> updateEmail(
            @PathVariable("email") String newEmail) {
        return ResponseEntity.ok().body(profileService.updateEmail(newEmail));
    }



    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Boolean delete(@PathVariable("id") Integer id,
                          @RequestHeader("Authorization") String token) {
        SecurityUtil.getJwtDTO(token, ProfileRole.ROLE_ADMIN);
        profileService.delete(id);
        return true;
    }


}

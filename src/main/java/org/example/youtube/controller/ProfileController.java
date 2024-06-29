package org.example.youtube.controller;

import jakarta.validation.Valid;
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


/*Profile (USER role)
	2. Update Email (with email verification)


    5. Get Profile Detail (id,name,surname,email,main_photo((url)))

   */


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


    @PutMapping("/current")
    public ResponseEntity<Boolean> updateUser(@RequestBody ProfileUpdateDTO profile,
                                              @RequestHeader("Authorization") String token) {
        JwtDTO dto = SecurityUtil.getJwtDTO(token,ProfileRole.ROLE_USER);
        profileService.updateUser(dto.getId(), profile);
        return ResponseEntity.ok().body(true);
    }


    @PutMapping("/updateEmail")
    public ResponseEntity<Boolean> UpdateEmail(@RequestBody String newemail,
                                                         @RequestHeader("Authorization") String token) {
        JwtDTO dto = SecurityUtil.getJwtDTO(token,ProfileRole.ROLE_USER);
        profileService.updateEmail(dto.getId(), newemail);
        return ResponseEntity.ok().body(true);
    }



    @DeleteMapping("/delete/{id}")
    public Boolean delete(@PathVariable("id") Integer id,
                          @RequestHeader("Authorization") String token) {
        SecurityUtil.getJwtDTO(token, ProfileRole.ROLE_ADMIN);
        profileService.delete(id);
        return true;
    }


}

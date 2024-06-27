package org.example.youtube.dto.profile;

import lombok.Getter;
import lombok.Setter;
import org.example.youtube.enums.ProfileRole;

@Getter
@Setter

public class ProfileCreateDTO {

    private String name;
    private String surname;
    private String email;
    private String password;
    private String photo_id;
    private ProfileRole role;
}

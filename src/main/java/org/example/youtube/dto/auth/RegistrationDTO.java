package org.example.youtube.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
        @NotBlank
    private String email;
 //   private String phone;
    @NotBlank
    private String password;
    private String photoId;
}

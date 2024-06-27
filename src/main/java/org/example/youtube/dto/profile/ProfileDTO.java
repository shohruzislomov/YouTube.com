package org.example.youtube.dto.profile;

import lombok.Getter;
import lombok.Setter;
import org.example.youtube.enums.ProfileRole;
import org.example.youtube.enums.ProfileStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProfileDTO {
    private Integer id;
    private String name;
    private String surName;
    private String email;
    private String password;
    private String photo;
    private ProfileRole role;
    private ProfileStatus status;
    private Boolean visible;
    private LocalDateTime createdDate;
    private String jwt;

}

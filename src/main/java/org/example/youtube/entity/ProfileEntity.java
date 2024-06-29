package org.example.youtube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.youtube.enums.ProfileRole;
import org.example.youtube.enums.ProfileStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "profile")
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "photo")
    private String photo;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private ProfileRole role;
    @Column(name = "temp_email" )
    private String tempEmail;
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProfileStatus status;
    @Column(name = "visible")
    private Boolean visible= Boolean.TRUE;;
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
}

package org.example.youtube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.youtube.enums.Status;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "channel")
public class ChannelEntity {
    @Id
    @UuidGenerator
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "banner_id")
    private String bannerId;
    @OneToOne
    @JoinColumn(name = "banner_id", insertable = false, updatable = false)
    private AttachEntity banner;

    @Column(name = "photo_id")
    private String photoId;
    @OneToOne
    @JoinColumn(name = "photo_id", insertable = false, updatable = false)
    private AttachEntity photo;

    @Column(name = "profile_id")
    private Long profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id",insertable=false, updatable=false)
    private ProfileEntity profile;

    @Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();


}

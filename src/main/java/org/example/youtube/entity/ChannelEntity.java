package org.example.youtube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.youtube.enums.ChannelStatus;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "channel")
@Getter
@Setter
public class ChannelEntity {
    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;


    @Column(name = "name")
    private String name;


    @Column(name = "description")
    private String description;


    @Column(name = "photo_id")
    private String photoId;


    @Column(name = "banner_id")
    private String bannerId;


    @Column(name = "owner_id")
    private Integer ownerId;



    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ChannelStatus status = ChannelStatus.ACTIVE;


    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;


}

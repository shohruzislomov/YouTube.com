package org.example.youtube.dto.channel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.example.youtube.dto.AttachDTO;
import org.example.youtube.dto.profile.ProfileDTO;
import org.example.youtube.enums.Status;

import java.time.LocalDateTime;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelDTO {
    private String id;
    private String name;
    private String description;
    private Status status;
    private String photoId;
    private AttachDTO photo;
    private String bannerId;
    private AttachDTO banner;
    private Long profileId;
    private ProfileDTO profile;
    private LocalDateTime created;
}

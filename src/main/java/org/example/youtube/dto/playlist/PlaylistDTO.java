package org.example.youtube.dto.playlist;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.example.youtube.dto.channel.ChannelDTO;
import org.example.youtube.dto.profile.ProfileDTO;
import org.example.youtube.enums.PlaylistStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistDTO {
    private Long id;
    private String name;
    private String description;
    private PlaylistStatus status;
    private String chanelId;
    private ChannelDTO chanel;
    private Long profileId;
    private ProfileDTO profile;
    private int orderNumber;
    private LocalDateTime createdDate;
    private Integer videoCount;
}

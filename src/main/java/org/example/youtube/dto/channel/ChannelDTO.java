package org.example.youtube.dto.channel;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.example.youtube.enums.ChannelStatus;

@Getter
@Setter
public class ChannelDTO {
    private String Id;
    private String name;
    private String description;
    private String photoId;
    private String bannerId;
    private ChannelStatus status=ChannelStatus.ACTIVE;
    private Integer ownerId;
    private Boolean visible = Boolean.TRUE;
}

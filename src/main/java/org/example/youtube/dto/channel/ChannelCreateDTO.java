package org.example.youtube.dto.channel;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

public class ChannelCreateDTO {
    @NotBlank(message = "name required")
    private String name;
    @NotBlank(message = "description required")
    private String description;
    @NotBlank(message = "photoId required")
    private String photoId;
    @NotBlank(message = "bannerId required")
    private String bannerId;
}

package org.example.youtube.dto.playlist;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistUpdateDTO {
    @Size(min = 3, message = "name must be at least 3 characters")
    private String name;

    @Size(min = 10, message = "description must be at least 10 characters")
    private String description;

    private Integer orderNumber;
}

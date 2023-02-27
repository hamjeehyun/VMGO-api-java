package vmgo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserConstellationDto {
    private String userConstellationId;

    private UserDto user;

    private ConstellationDto constellation;

    @Builder
    public UserConstellationDto(String userConstellationId, UserDto user, ConstellationDto constellation) {
        this.userConstellationId = userConstellationId;
        this.user = user;
        this.constellation = constellation;
    }
    
    
}

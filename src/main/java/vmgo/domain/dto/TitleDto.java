package vmgo.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vmgo.domain.common.BaseTimeDto;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class TitleDto extends BaseTimeDto<TitleDto> {
    private String titleId;
    private String titleName;
    private String challengeId;

    @Builder
    public TitleDto(String titleId, String titleName, String challengeId){
        this.titleId = titleId;
        this.titleName = titleName;
        this.challengeId = challengeId;
    }

}

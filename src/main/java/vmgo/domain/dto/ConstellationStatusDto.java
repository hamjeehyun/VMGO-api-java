package vmgo.domain.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ConstellationStatusDto {
    private int level;
    private int step;
    private List<ConstellationJoinDto.Response> list ;

    @Builder
    public ConstellationStatusDto(int level, int step, List<ConstellationJoinDto.Response> list ) {
        this.level = level;
        this.step = step;
        this.list  = list ;
    }
}

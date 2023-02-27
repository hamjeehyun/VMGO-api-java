package vmgo.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDto<T> {
    private T value;

    public ResponseDto(T value) {
        this.value = value;
    }
}

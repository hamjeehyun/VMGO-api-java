package vmgo.domain.common;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class CommonResponseDto<T> implements Serializable  {
	private String code;
	private T payload;
	
	public CommonResponseDto (T payload) {
		this.payload = payload;
	}
}

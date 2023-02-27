package vmgo.util;

import org.apache.http.HttpStatus;

import vmgo.domain.common.CommonResponseDto;

public class ResponseUtil {

	public static <T> CommonResponseDto<T> success(T response){
		return new CommonResponseDto(String.valueOf(HttpStatus.SC_OK), response);
	}
	
	public static <T> CommonResponseDto<T> fail(String code, T response){
		return new CommonResponseDto(code, response);
	}
} 
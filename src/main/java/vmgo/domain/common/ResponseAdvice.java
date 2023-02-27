package vmgo.domain.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import vmgo.util.ResponseUtil;
import vmgo.util.StringUtil;

/**
 * @author RUBY
 * @date 2022/07/25
 * @description Response 공통 포맷 wrapping용 advice<br>
 * RestController에서 반환되는 response를<br>
 * status / payload를 각각 적재하여 돌려줌<br>
 */
@RestControllerAdvice(annotations = RestController.class)
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
	private final static Logger LOGGER = LoggerFactory.getLogger(ResponseAdvice.class);

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		
		if ( StringUtil.equals(returnType.getParameterType().getName(), "vmgo.domain.common.CommonResponseDto") ) {
			return false;
		}
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		return ResponseUtil.success(body);
		//return body;
	}
	
	@ExceptionHandler()
	public Object errorHandler(OnfException e) {
		LOGGER.error(e.getMessage(), e);
		return ResponseUtil.fail(e.getCode(), e.getMessage());
	}

}

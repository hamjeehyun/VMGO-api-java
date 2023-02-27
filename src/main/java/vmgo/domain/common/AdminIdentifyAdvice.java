package vmgo.domain.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import vmgo.domain.dto.UserDto;
import vmgo.domain.dto.UserRole;
import vmgo.util.ExceptionUtil;

/**
 * @packageName vmgo.domain.common
 * @fileName AdminIdentifyAdvice.java
 * @author RUBY
 * @date 2022/08/27
 * @description Admin 권한 확인용 Advice<br>
 * ~ForAdmin이름으로 형성된 메서드를 호출하는 요청에 대한 권한 검증을 실시한다.<br>
 * ================================<br>
 * DATE				AUTHOR			NOTE<br>
 * 2022/08/27 		 RUBY			최초생성
 */
@Aspect
@Component
public class AdminIdentifyAdvice {
	private final static Logger LOGGER = LoggerFactory.getLogger(AdminIdentifyAdvice.class);

	@Before("execution(* vmgo.controller.*Controller.*ForAdmin(..))")
	public void testAdvice(JoinPoint joinPoint) {
		LOGGER.info("============= AdminIdentifyAdvice NOW RUNNING =============");
		LOGGER.info("=====" + joinPoint.toString()+"=====");
		UserDto loginUser = null;
		
		if ( SecurityContextHolder.getContext().getAuthentication() == null ) loginUser = new UserDto("AuthenticationNULL", UserRole.UNIDENTIFIED);
		else {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDto)  loginUser = (UserDto)principal;
			else  loginUser = new UserDto("principalNULL", UserRole.UNIDENTIFIED);
		}
		
		if ( UserRole.ADMIN != loginUser.getUserRole() ) {
			throw ExceptionUtil.createOnfBizException("ONF_0000", loginUser.getUserId());
		} else {
			LOGGER.info(loginUser.toString());
		}
	}
}
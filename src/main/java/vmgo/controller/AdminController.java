package vmgo.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import vmgo.domain.common.CommonResponseDto;
import vmgo.service.AdminService;
import vmgo.util.StringUtil;

/**
 * @packageName vmgo.controller
 * @fileName AdminController.java
 * @author RUBY
 * @date 2022/07/29
 * @description 채번 및 admin로그인용 컨트롤러<br>
 * ================================<br>
 * DATE				AUTHOR			NOTE<br>
 * 2022/07/29 		 RUBY			최초생성
 */
@RestController
@RequestMapping("vmgo")
public class AdminController {
	private final static Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	private AdminService adminService;
	
	/**
	 * @deprecated 개발용. 사용안함
	 * @param tableName
	 * @return
	 */
	@GetMapping("key/{tableName}")
	public Map<String, Object> getGeneratedKeyForAdmin(@PathVariable(name="tableName")String tableName) {
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("generatedKey", adminService.getGeneratedKey(tableName.toUpperCase()));
		return rtnMap;
	}
	
	/**
	 * 관리자용 비번을 확인한다
	 * @param <T>
	 * @param password
	 * @return true/false
	 */
	@PostMapping("new-world")
	@ApiOperation(value="관리자 인증 처리", notes="우린간다 신세계로 HIGHER")
	public <T> CommonResponseDto<T> diveInToNewWorldForAdmin(@RequestParam String password) {
		return new CommonResponseDto("200",
				StringUtil.equals(password, "fkdlcmdhs!"));
	}
}

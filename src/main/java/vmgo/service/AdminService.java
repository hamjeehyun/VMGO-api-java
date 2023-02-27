package vmgo.service;

import java.util.Map;

/**
 * @packageName vmgo.service
 * @fileName AdminService.java
 * @author RUBY
 * @date 2022/07/29
 * @description 어드민 서비스 인터페이스<br>
 * ================================<br>
 * DATE				AUTHOR			NOTE<br>
 * 2022/07/29 		 RUBY			최초생성
 */
public interface AdminService {

	/**
	 * @param tableName 채번할테이블이름
	 * @return 채번된 키
	 */
	Map<String, Object> getGeneratedKey(String tableName);
	
}

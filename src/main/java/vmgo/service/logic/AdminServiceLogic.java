package vmgo.service.logic;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vmgo.service.AdminService;
import vmgo.store.AdminStore;

/**
 * @packageName vmgo.service.logic
 * @fileName AdminServiceLogic.java
 * @author RUBY
 * @date 2022/07/29
 * @description 어드민 서비스 구현<br>
 * ================================<br>
 * DATE				AUTHOR			NOTE<br>
 * 2022/07/29 		 RUBY			최초생성
 */
@Service
public class AdminServiceLogic implements AdminService {
	private final static Logger LOGGER = LoggerFactory.getLogger(AdminServiceLogic.class);
	
	@Autowired
	private AdminStore adminStore;

	@Override
	public Map<String,Object> getGeneratedKey(String tableName) {
		Map<String,Object> map = new HashMap<>();
		map.put("generatedKey", adminStore.getGeneratedKey(tableName));
		return map;
	}

}

package vmgo.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import vmgo.domain.dto.GroupWatchingDto;
import vmgo.service.AdminService;
import vmgo.service.GroupWatchingService;

/**
 * @packageName vmgo.controller
 * @fileName GroupWatchingController.java
 * @author RUBY
 * @date 2022/07/25
 * @description 단체관람 REST 컨트롤러
 * ================================
 * DATE				AUTHOR			NOTE
 * 2022/07/25 		 RUBY			최초생성
 */
@RestController
@RequestMapping("vmgo/group-watching")
public class GroupWatchingController {
	private final static Logger LOGGER = LoggerFactory.getLogger(GroupWatchingController.class);
	
	private final GroupWatchingService groupWatchingService;
	private final AdminService adminService;

    public GroupWatchingController(GroupWatchingService groupWatchingService, AdminService adminService) {
        this.groupWatchingService = groupWatchingService;
        this.adminService = adminService;
    }
	
	/**
	 * 상영중인 / 상영예정인 단관 리스트를 반환한다(for User)
	 * @return Map&ltString,Object&gt
	 */
	@GetMapping
	@ApiOperation(value="단관 리스트 조회(for User)", notes="상영중·상영예정인 단관 리스트를 각각 4건까지 반환한다(active=true ONLY)")
	public Map<String,Object> findAllGroupWatchingForUser() {
		return groupWatchingService.findAllGroupWatchingForUser();
	}
	
	/**
	 * 모든 단관 리스트를 반환한다(for Admin)
	 * @return Map&ltString,Obejct&gt
	 */
	@GetMapping("admin")
	@ApiOperation(value="단관 리스트 조회(for Admin)", notes="모든 단관리스트를 반환한다")
	public List<GroupWatchingDto> findAllGroupWatchingForAdmin(){
		return groupWatchingService.findAllGroupWatching();
	}
	
	/**
	 * 넘겨받은 ID로 조회한<br>
	 * 단관 정보와 비디오 정보를 반환<br>
	 * ID가 잘못됐을 시 조회불가 메시지를 반환
	 * @param id 단관 ID
	 * @return GroupWatchingDto
	 */
	@GetMapping("{id}")
	@ApiOperation(value="단관 단건 조회", notes="ID에 해당하는 단관정보·단관비디오 정보를 조회한다")
	public GroupWatchingDto findGroupWatchingById(@PathVariable(name="id") String id) {
		return groupWatchingService.findGroupWatchingById(id);
	}
	
	/**
	 * 전달받은 단제관람 정보를 DB에 저장한다
	 * @param groupWatchingDto
	 * @return GroupWatchingDto 
	 * - 정상 등록된 단관DTO
	 */
	@PostMapping
	@ApiOperation(value="단관 등록", notes="전달받은 단제관람 정보를 DB에 저장한다")
	public GroupWatchingDto registGroupWatchingForAdmin(@RequestBody GroupWatchingDto groupWatchingDto) {
		return groupWatchingService.insertGroupWatching(groupWatchingDto);
	}
	
	/**
	 * 전달받은 단체관람 정보를<br>
	 * watchingId를 기준으로 DB에 업데이트한다
	 * @param groupWatchingDto
	 * @return GroupWatchingDto
	 * - 정상 업데이트된 단관DTO
	 */
	@PutMapping
	@ApiOperation(value="단관 업데이트", notes="전달받은 단제관람 정보를 DB에 업데이트한다")
	public GroupWatchingDto updateGroupWatchingForAdmin(@RequestBody GroupWatchingDto groupWatchingDto) {
		return groupWatchingService.updateGroupWatching(groupWatchingDto);
	}
	
	/**
	 * endDate가 없는 단관리스트를 셀렉하여<br>
	 * startDate + video.duration을 조합하여<br>
	 * endDate를 업데이트한다.
	 * @return Map&ltString, Object&gt<br>
	 * totalLine : 전체 라인 수<br>
	 * updateLine : 갱신성공한 라인 수<br>
	 */
	@Deprecated
	@PostMapping("update")
	@ApiOperation(value="단관 종료시각 업데이트", notes="종료시각이 비어있는 단관을 조회하여 종료시각을 업데이트한다")
	public Map<String, Object> updateEndDateForAdmin(){
		return groupWatchingService.updateEndDate();
	}
	
	/**
	 * 자동생성한 단체관람 기본키를 받아온다
	 * @return Map&ltString, Object&gt
	 * genaratedKey : 채번된 키
	 */
	@GetMapping("key")
	@ApiOperation(value="단관 기본키 채번", notes="단관 기본키를 채번한다")
	public Map<String, Object> getGroupWatchingGeneratedKeyForAdmin(){
		return adminService.getGeneratedKey("GROUP_WATCHING");
	}
}

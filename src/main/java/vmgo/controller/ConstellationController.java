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
import vmgo.domain.dto.ConstellationDto;
import vmgo.domain.dto.ConstellationStatusDto;
import vmgo.domain.dto.UserConstellationDto;
import vmgo.service.AdminService;
import vmgo.service.ConstellationService;

/**
 * @packageName vmgo.controller
 * @fileName ConstellationController.java
 * @author RUBY
 * @date 2022/08/14
 * @description 별자리 REST 컨트롤러<br>
 * ================================<br>
 * DATE				AUTHOR			NOTE<br>
 * 2022/08/14 		 RUBY			최초생성
 */
@RestController
@RequestMapping("vmgo/constellation")
public class ConstellationController {
	private final static Logger LOGGER = LoggerFactory.getLogger(ConstellationController.class);
	
	private final ConstellationService constellationService;
	private final AdminService adminService;

    public ConstellationController(ConstellationService constellationService, AdminService adminService) {
    	this.constellationService = constellationService;
        this.adminService = adminService;
    }
    
	/**
	 * 자동생성한 단체관람 기본키를 받아온다
	 * @return Map&ltString, Object&gt
	 * genaratedKey : 채번된 키
	 */
	@GetMapping("key")
	@ApiOperation(value="별자리 기본키 채번", notes="별자리 기본키를 채번한다")
	public Map<String, Object> getConstellationGeneratedKeyForAdmin(){
		return adminService.getGeneratedKey("CONSTELLATION");
	}
	
	/**
	 * 모든 별자리 리스트를 반환한다(for Admin)
	 * @return Map&ltString,Obejct&gt
	 */
	@GetMapping("admin")
	@ApiOperation(value="별자리 리스트 조회(for Admin)", notes="모든 별자리 리스트를 반환한다")
	public List<ConstellationDto> findConstellationListForAdmin(){
		return constellationService.findAllConstellation();
	}
	
	/**
	 * 넘겨받은 ID로 조회한<br>
	 * 별자리 정보를 반환<br>
	 * ID가 잘못됐을 시 조회불가 메시지를 반환
	 * @param id
	 * @return ConstellationDto
	 */
	@GetMapping("{id}")
	@ApiOperation(value="별자리 단건 조회", notes="ID에 해당하는 별자리 정보를 조회한다")
	public ConstellationDto findConstellationByIdForAdmin(@PathVariable(name="id") String id) {
		return constellationService.findConstellationById(id);
	}
	
	/**
	 * 전달받은 별자리 정보를 DB에 저장한다
	 * @param ConstellationDto
	 * @return ConstellationDto 
	 * - 정상 등록된 별자리DTO
	 */
	@PostMapping
	@ApiOperation(value="별자리 등록", notes="전달받은 별자리 정보를 DB에 저장한다")
	public ConstellationDto registConstellationForAdmin(@RequestBody ConstellationDto constellationDto) {
		return constellationService.insertConstellation(constellationDto);
	}
	
	/**
	 * 전달받은 별자리 정보를 DB에 업데이트한다
	 * @param constellationDto
	 * @return ConstellationDto
	 * - 정상 수정된 별자리DTO
	 */
	@PutMapping
	@ApiOperation(value="별자리 수정", notes="전달받은 별자리 정보를 DB에 업데이트한다")
	public ConstellationDto updateConstellationForAdmin(@RequestBody ConstellationDto constellationDto) {
		return constellationService.updateConstellation(constellationDto);
	}
	
	/**
	 * 유저의 별자리 획득처리를 수행한다.
	 * @param constellationId
	 * @param uid
	 * @return ConstellationDto
	 */
	@PostMapping("{constellationId}/{uid}")
	@ApiOperation(value="별자리 획득(for User)", notes="유저의 별자리 획득처리를 수행한다")
	public UserConstellationDto registUserConstellation(@PathVariable(name="constellationId") String constellationId,
											@PathVariable(name="uid")String uid) {
		return constellationService.insertUserConstellation(constellationId, uid);
	}
	
	/**
	 * 현제 active=true인 별자리 리스트에
	 * 유저 획득여부를 함께 담아 반환한다.
	 * @param uid
	 * @return ConstellationStatusDto
	 */
	@GetMapping("user/{uid}")
	@ApiOperation(value="별자리 리스트 조회(for User)", 
	notes="active=true인 별자리 리스트에 유저획득여부를 표시하여 반환한다\n"
			+ "유저가 모든 별자리를 획득했을 경우 level에 -1을 담아 반환한다")
	public ConstellationStatusDto findConstellationListByUid(@PathVariable(name="uid")String uid) {
		return constellationService.findConstellationByUid(uid);
	}
}

package vmgo.service;

import java.util.List;

import vmgo.domain.dto.ConstellationDto;
import vmgo.domain.dto.ConstellationStatusDto;
import vmgo.domain.dto.UserConstellationDto;

/**
 * @packageName vmgo.service
 * @fileName ConstellationService.java
 * @author RUBY
 * @date 2022/08/14
 * @description 별자리 서비스 인터페이스<br>
 * ================================<br>
 * DATE				AUTHOR			NOTE<br>
 * 2022/08/14 		 RUBY			최초생성
 * 2022/08/20 		 RUBY			유저 별자리 조회 / 유저 별자리 획득 로직 작성
 */
public interface ConstellationService {

	/**
	 * 모든 별자리 리스트를 반환한다<br>
	 * (Admin용 / active 무관)
	 * @return List&ltConstellationDto&gt
	 */
	List<ConstellationDto> findAllConstellation();

	/**
	 * 넘겨받은 ID 로 조회한 별자리 단건을 반환
	 * @param id
	 * @return ConstellationDto
	 */
	ConstellationDto findConstellationById(String id);

	/**
	 * 전달받은 DTO 정보를 DB에 저장한다
	 * @param constellationDto
	 * @return ConstellationDto
	 */
	ConstellationDto insertConstellation(ConstellationDto constellationDto);

	/**
	 * 전달받은 DTO 정보를 DB에 업데이트한다
	 * @param constellationDto
	 * @return ConstellationDto
	 */
	ConstellationDto updateConstellation(ConstellationDto constellationDto);

	/**
	 * 유저가 보유하고 있는 별자리 정보를 리턴한다.
	 * @param uid
	 * @return ConstellationStatusDto
	 */
	ConstellationStatusDto findConstellationByUid(String uid);

	/**
	 * 유저가 보유하고 있는 별자리 정보를 리턴한다.
	 * @param uid
	 * @return ConstellationStatusDto
	 */
	ConstellationStatusDto findConstellationByUidOld(String uid);

	/**
	 * 특정 유저의 별자리를 획득처리한다.
	 * @param constellationId
	 * @param uid
	 * @return ConstellationDto
	 */
	UserConstellationDto insertUserConstellation(String constellationId, String uid);
}

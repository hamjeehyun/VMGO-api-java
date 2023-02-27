package vmgo.store;

import java.util.List;

import vmgo.domain.dto.ConstellationDto;
import vmgo.domain.dto.ConstellationJoinDto;
import vmgo.domain.interfaces.UserConstellationInterface;
import vmgo.store.entity.Constellation;

public interface ConstellationStore {
    List<ConstellationDto> findConstellationByUid(String uid);

	/**
	 * 모든 별자리 리스트를 반환한다<br>
	 * (Admin용 / active 무관)
	 * @return Map&ltString, Object&gt
	 */
    List<Constellation> findAllConstellation();

	/**
	 * 넘겨 받은 id로 조회한 별자리 단건을 반환한다
	 * @param id
	 * @return Constellation
	 */
	Constellation findConstellationById(String id);

	/**
	 * 넘겨받은 정보로 별자리 단건을 저장한다
	 * @param constellation
	 * @return Constellation
	 */
	Constellation saveConstellation(ConstellationDto constellationDto);
	
	/**
	 * 넘겨받은 정보로 별자리 리스트와
	 * 해당별자리의 유저 보유여부를 반환한다.
	 * @param uid
	 * @return List&ltUserConstellationInterface&gt
	 */
	List<UserConstellationInterface> findConstellationListAndUserConstellStatus(String uid);

	/**
	 * 넘겨받은 정보로 별자리 리스트와
	 * 해당별자리의 유저 보유여부를 반환한다.
	 * @param uid
	 * @return List&ltUserConstellationInterface&gt
	 */
	List<UserConstellationInterface> findConstellationListByUser(String uid);
}

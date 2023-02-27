package vmgo.store.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vmgo.domain.dto.ConstellationJoinDto;
import vmgo.domain.interfaces.UserConstellationInterface;
import vmgo.store.entity.Constellation;
import vmgo.store.entity.User;

@Repository
public interface ConstellationRepository extends JpaRepository<Constellation, String>, JpaSpecificationExecutor<Constellation> {
	
	/**
	 * 모든 별자리 리스트를 반환<br>
	 * (별자리 ID를 기준으로 DESC)
	 * @return List&ltConstellation&gt
	 */
	@Query("SELECT c FROM Constellation c ORDER BY c.constellationId DESC")
	List<Constellation> findAllConstellation();

	/**
	 * 넘겨받은 정보로 별자리 리스트와
	 * 해당별자리의 유저 보유여부를 반환한다.<br/>
	 * (userConId 가 null일 경우 미보유)
	 * @param user
	 * @return List&ltUserConstellationInterface&gt
	 */
	@Query(value = "SELECT c.level as conLevel, c.step as conStep, c.image_path as imagePath, uc.constellation_id as userConId, c.constellation_id as constellationId "
			  + ",SUM(c.step) OVER (ORDER BY c.level) as totalStep "	
		      + "FROM vmgo.constellation c "
		      + "LEFT JOIN (SELECT * FROM vmgo.user_constellation u WHERE u.uid = :uid) uc ON c.constellation_id = uc.constellation_id "
		      + "WHERE c.active = true "
		      + "AND ( uc.uid = :uid OR uc.uid IS NULL ) "
		      + "ORDER BY c.level ASC", nativeQuery = true)
	List<UserConstellationInterface> findConstellationListAndUserConstellStatus(@Param(value="uid")User user);
	
	/**
	 * 넘겨받은 정보로 별자리 리스트와
	 * 해당별자리의 유저 보유여부를 반환한다.<br/>
	 * (userConId 가 null일 경우 미보유)
	 * @param user
	 * @return List&ltUserConstellationInterface&gt
	 */
	@Query(value="SELECT "
			+ "CASE "
			+ "	WHEN uc.uid IS NOT NULL THEN 'COMPLETE' "
			+ "	WHEN SUM(c.step) OVER(ORDER BY c.level) <= (SELECT COUNT(*) FROM vmgo.video_status vs WHERE vs.status = 1 AND vs.uid = :uid) then 'READY' "
			+ "	WHEN (SUM(c.step) OVER(ORDER BY c.level) - (SELECT COUNT(*) FROM vmgo.video_status vs WHERE vs.status = 1 AND vs.uid = :uid)) < c.step then 'ONGOING' "
			+ "	ELSE 'LOCKED' "
			+ "END as status "
			+ ",c.constellation_id as constellationId "
			+ ",c.level as conLevel "
			+ ",c.step as conStep "
			+ ",c.title as conTitle "
			+ ",c.description as conDescription "
			+ ",c.component_name as conCompName "
			+ ",c.image_path as imagePath "
			+ ",uc.user_constellation as userConId "
			//+ ",SUM(c.step) OVER (ORDER BY c.level) as totalStep "
			+ "FROM vmgo.constellation c "
			+ "LEFT JOIN (SELECT * FROM vmgo.user_constellation u WHERE u.uid = :uid) uc ON c.constellation_id = uc.constellation_id "
			+ "WHERE c.active = 1 "
			+ "ORDER BY c.level ASC ", nativeQuery = true)
	List<UserConstellationInterface> findConstellationListByUser(@Param(value="uid")User user);
}

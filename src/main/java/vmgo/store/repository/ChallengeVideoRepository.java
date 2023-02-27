package vmgo.store.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vmgo.domain.interfaces.VideoStatusInChallengeInterface;
import vmgo.store.entity.Challenge;
import vmgo.store.entity.ChallengeVideo;

@Repository
public interface ChallengeVideoRepository extends JpaRepository<ChallengeVideo, String>, JpaSpecificationExecutor<ChallengeVideo> {
    List<ChallengeVideo> findAllByChallenge(Challenge challenge);
    int countByChallenge(Challenge challenge);
    @Transactional
    void deleteByChallenge(Challenge challenge);
    
    @Query(value = "SELECT "
    		+ "COUNT(cv.video_id) as allVideo, "
    		+ "SUM(CASE WHEN vs.status = 1 THEN 1 ELSE 0 END) as endVideo "
    		+ "FROM vmgo.challenge_video cv "
    		+ "LEFT JOIN (SELECT * FROM vmgo.video_status v WHERE v.uid = :uid) vs ON cv.video_id = vs.video_id "
    		+ "WHERE cv.challenge_id = :challengeId", nativeQuery=true)
	VideoStatusInChallengeInterface findAllVideoStatusInChallenge(@Param(value="challengeId")Challenge challenge
									,@Param(value="uid") String uid);
}

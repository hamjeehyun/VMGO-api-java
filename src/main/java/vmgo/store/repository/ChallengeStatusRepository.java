package vmgo.store.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vmgo.store.entity.Challenge;
import vmgo.store.entity.ChallengeStatus;
import vmgo.store.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChallengeStatusRepository extends JpaRepository<ChallengeStatus, String>, JpaSpecificationExecutor<ChallengeStatus> {
    ChallengeStatus findByChallengeAndUser(Challenge challenge, User user);
    int countByChallenge(Challenge challenge);
    ChallengeStatus findFirstByUserAndStatusOrderByUpdatedDesc(User user, boolean status);
    ChallengeStatus findByUserAndUpdated(User user, LocalDateTime updated);
    List<ChallengeStatus> findAllByUser(User user);
    List<ChallengeStatus> findAllByUserAndStatusIsFalse(User user);
}

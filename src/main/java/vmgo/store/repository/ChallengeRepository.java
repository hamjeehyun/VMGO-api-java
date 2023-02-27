package vmgo.store.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vmgo.store.entity.Challenge;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, String>, JpaSpecificationExecutor<Challenge> {

}

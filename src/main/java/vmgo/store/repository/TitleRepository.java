package vmgo.store.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vmgo.store.entity.Title;

import java.util.List;

@Repository
public interface TitleRepository extends JpaRepository<Title, String>, JpaSpecificationExecutor<String> {
    Title findTitleByChallengeId(String challengeId);
}

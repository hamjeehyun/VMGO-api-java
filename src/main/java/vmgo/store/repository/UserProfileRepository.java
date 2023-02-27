package vmgo.store.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vmgo.store.entity.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String>, JpaSpecificationExecutor<UserProfile> {
    UserProfile findByUid(String uid);
}

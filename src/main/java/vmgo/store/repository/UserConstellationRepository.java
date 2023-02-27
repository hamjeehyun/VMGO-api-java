package vmgo.store.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vmgo.store.entity.Constellation;
import vmgo.store.entity.User;
import vmgo.store.entity.UserConstellation;

@Repository
public interface UserConstellationRepository extends JpaRepository<UserConstellation, String>, JpaSpecificationExecutor<UserConstellation> {

	UserConstellation findByConstellationAndUser(Constellation constellation, User user);
}

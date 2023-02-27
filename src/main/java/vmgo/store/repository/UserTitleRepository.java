package vmgo.store.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vmgo.store.entity.Title;
import vmgo.store.entity.User;
import vmgo.store.entity.UserTitle;

import java.util.List;

@Repository
public interface UserTitleRepository extends JpaRepository<UserTitle, String>, JpaSpecificationExecutor<UserTitle> {
    List<UserTitle> findAllByUser(User user);
    boolean existsByTitleAndUser(Title title, User user);
}

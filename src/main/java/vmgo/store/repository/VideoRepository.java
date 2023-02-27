package vmgo.store.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vmgo.store.entity.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, String>, JpaSpecificationExecutor<Video> {
}

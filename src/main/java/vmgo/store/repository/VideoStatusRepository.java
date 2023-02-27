package vmgo.store.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vmgo.store.entity.VideoStatus;

import java.util.List;

@Repository
public interface VideoStatusRepository extends JpaRepository<VideoStatus, String>, JpaSpecificationExecutor<VideoStatus> {
    VideoStatus findByUidAndVideoId(String uid, String videoId);
    boolean existsByUidAndVideoIdAndStatusIsTrue(String uid, String videoId);
    VideoStatus findFirstByUidAndStatusOrderByUpdatedDesc(String uid, boolean status);
	int countByUidAndStatus(String uid, boolean b);
}

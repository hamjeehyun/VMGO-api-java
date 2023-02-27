package vmgo.store.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vmgo.domain.dto.Reaction;
import vmgo.store.entity.VideoReaction;

@Repository
public interface VideoReactionRepository extends JpaRepository<VideoReaction, String>, JpaSpecificationExecutor<VideoReaction> {
    List<VideoReaction> findAllByVideoIdAndUid(String videoId, String uid);
    List<VideoReaction> findAllByVideoId(String videoId);
    @Transactional
    void deleteByVideoIdAndUidAndReaction(String videoId, String uid, Reaction reaction);
	Object countByVideoIdAndReaction(String videoId, Reaction reaction);
	VideoReaction findByVideoIdAndUidAndReaction(String videoId, String uid, Reaction reaction);
}
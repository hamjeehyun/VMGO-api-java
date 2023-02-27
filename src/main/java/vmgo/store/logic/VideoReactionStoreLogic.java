package vmgo.store.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import vmgo.domain.dto.Reaction;
import vmgo.domain.dto.VideoReactionDto;
import vmgo.store.VideoReactionStore;
import vmgo.store.entity.VideoReaction;
import vmgo.store.repository.VideoReactionRepository;

@Repository
public class VideoReactionStoreLogic implements VideoReactionStore {
	private final static Logger LOGGER = LoggerFactory.getLogger(VideoReactionStoreLogic.class);
	
    private final VideoReactionRepository repository;
    private final EntityManager em;

    public VideoReactionStoreLogic(VideoReactionRepository repository, EntityManager em) {
        this.repository = repository;
        this.em = em;
    }

    @Override
    @Transactional
    public void saveVideoReaction(VideoReactionDto videoReactionDto) {
        VideoReaction videoReaction = new VideoReaction(videoReactionDto);
        //videoReaction.setUpdated(LocalDateTime.now());

        em.persist(videoReaction);
        //repository.save(videoReaction);
        //return videoReaction.getVideoReactionId();
    }

    @Override
    public List<VideoReactionDto> findVideoReactionByVideoIdAndUid(String videoId, String uid) {
        List<VideoReaction> videoReactionList = repository.findAllByVideoIdAndUid(videoId, uid);
        return videoReactionList.stream().map(VideoReaction::toDto).collect(Collectors.toList());
    }

    @Override
    public Map<Reaction, Object> findAllByVideoId(String videoId) {
        Map<Reaction,Object> map = new HashMap<>();
        
        for ( Reaction key : Reaction.values() ) {
        	map.put(key, repository.countByVideoIdAndReaction(videoId, key));
        }
        
        return map;
    }

    @Override
    public void deleteVideoReactionByVideoIdAndUidAndReaction(VideoReactionDto videoReactionDto) {
        repository.deleteByVideoIdAndUidAndReaction(videoReactionDto.getVideoId() 
        											,videoReactionDto.getUid()
        											,videoReactionDto.getReaction());
    }

	@Override
	public VideoReaction findVideoReactionByVideoIdAndUidAndReaction(VideoReactionDto videoReactionDto) {
		return repository.findByVideoIdAndUidAndReaction(videoReactionDto.getVideoId()
														,videoReactionDto.getUid()
														,videoReactionDto.getReaction());
	}

	@Override
	public Map<Reaction, Object> selectReactionsByVideoId(String videoId) {
		Map<Reaction, Object> rtnMap = new HashMap<>();
		
		String queryStr = "SELECT "
				+ "SUM(CASE WHEN vr.reaction = 'LOVE' THEN 1 ELSE 0 END), "
				+ "SUM(CASE WHEN vr.reaction = 'AWESOME' THEN 1 ELSE 0 END), "
				+ "SUM(CASE WHEN vr.reaction = 'LIKE' THEN 1 ELSE 0 END), "
				+ "SUM(CASE WHEN vr.reaction = 'TOUCHING' THEN 1 ELSE 0 END), "
				+ "SUM(CASE WHEN vr.reaction = 'EXCITING' THEN 1 ELSE 0 END), "
				+ "SUM(CASE WHEN vr.reaction = 'CHEER' THEN 1 ELSE 0 END) "
			+ "FROM VideoReaction vr "
			+ "WHERE vr.videoId = :videoId";
		
		@SuppressWarnings("unchecked")
		List<Object[]> result = em.createQuery(queryStr).setParameter("videoId", videoId).getResultList();
		Object[] resultRow = result.get(0);
		
		for ( Reaction key : Reaction.values() ) {
			rtnMap.put(key, resultRow[key.ordinal()]);
		}
		
		return rtnMap;
	}
}

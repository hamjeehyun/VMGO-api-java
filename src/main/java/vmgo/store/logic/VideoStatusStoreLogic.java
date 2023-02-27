package vmgo.store.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import vmgo.domain.dto.VideoStatusDto;
import vmgo.store.VideoStatusStore;
import vmgo.store.entity.VideoStatus;
import vmgo.store.repository.VideoStatusRepository;
import vmgo.util.JsonUtil;

import java.time.LocalDateTime;

@Repository
public class VideoStatusStoreLogic implements VideoStatusStore {
    private final static Logger LOGGER = LoggerFactory.getLogger(VideoStatusStoreLogic.class);

    private final VideoStatusRepository repository;

    public VideoStatusStoreLogic(VideoStatusRepository repository) {
        this.repository = repository;
    }

    @Override
    public String saveVideoStatus(VideoStatusDto videoStatusDto) {
        VideoStatus videoStatus = new VideoStatus(videoStatusDto);
        videoStatus.setUpdated(LocalDateTime.now());
        repository.save(videoStatus);
        return videoStatus.getVideoStatusId();
    }

    @Override
    public VideoStatusDto findProgressById(String id) {
        VideoStatus videoStatus = repository.findById(id).orElse(null);
        return videoStatus.toDto();
    }

    @Override
    public VideoStatusDto findVideoStatusByUidAndVideoId(String uid, String videoId) {
        VideoStatus videoStatus = repository.findByUidAndVideoId(uid, videoId);

        if(videoStatus == null) {
            return null;
        }

        return videoStatus.toDto();
    }

    @Override
    public boolean existsByUidAndVideoIdAndWatchingStatusIsTrue(String uid, String videoId) {
        return repository.existsByUidAndVideoIdAndStatusIsTrue(uid, videoId);
    }

    @Override
    public VideoStatusDto findVideoStatusFirstByUidAndStatusOrderByUpdated(String uid, boolean status) {
        VideoStatus videoStatus = repository.findFirstByUidAndStatusOrderByUpdatedDesc(uid, status);
        if(videoStatus == null){
            return null;
        }
        return videoStatus.toDto();
    }

	@Override
	public int countByUidAndStatus(String uid, boolean b) {
		return repository.countByUidAndStatus(uid, b);
	}
}

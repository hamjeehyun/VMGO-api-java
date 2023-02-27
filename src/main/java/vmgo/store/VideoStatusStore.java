package vmgo.store;

import vmgo.domain.dto.VideoStatusDto;

public interface VideoStatusStore {
    String saveVideoStatus(VideoStatusDto videoStatusDto);
    VideoStatusDto findProgressById(String id);
    VideoStatusDto findVideoStatusByUidAndVideoId(String uid, String videoId);
    boolean existsByUidAndVideoIdAndWatchingStatusIsTrue(String uid, String videoId);
    VideoStatusDto findVideoStatusFirstByUidAndStatusOrderByUpdated(String uid, boolean status);
	int countByUidAndStatus(String uid, boolean b);
}

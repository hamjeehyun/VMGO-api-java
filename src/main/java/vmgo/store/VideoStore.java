package vmgo.store;

import vmgo.domain.dto.VideoDto;

import java.util.List;

public interface VideoStore {
    List<VideoDto> findAllVideo();
    VideoDto findVideoById(String id);
}

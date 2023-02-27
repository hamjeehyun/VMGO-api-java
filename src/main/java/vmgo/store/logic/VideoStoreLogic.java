package vmgo.store.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import vmgo.domain.dto.VideoDto;
import vmgo.store.VideoStore;
import vmgo.store.entity.Video;
import vmgo.store.repository.VideoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class VideoStoreLogic implements VideoStore {
    @Autowired
    private VideoRepository repository;

    @Override
    public List<VideoDto> findAllVideo() {
        List<Video> videoList = repository.findAll();
        return videoList.stream().map(Video::toDto).collect(Collectors.toList());
    }

    @Override
    public VideoDto findVideoById(String id) {
        Video video = repository.findById(id).orElse(null);
        return video.toDto();
    }
}

package vmgo.store.entity;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import vmgo.domain.dto.VideoDto;
import vmgo.store.entity.common.BaseTimeEntity;
import vmgo.util.JsonUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table
public class Video extends BaseTimeEntity<VideoDto> {
    @Id @Column(name = "video_id", length = 100)
    private String videoId;
    private String title;
    private String date;
    private long duration;
    private String member;
    private String guest;
    private String tag;
    private String description;
    private String thumbnail;
    private int views;
    private String vLiveLink;

    public Video() {
        super();
    }

    public Video(VideoDto videoDto) {
        super(videoDto);
    }

    @Override
    public void update(VideoDto dto) {
        BeanUtils.copyProperties(dto, this);

        if (dto.getMember() != null) {
            member = JsonUtil.toJson(dto.getMember());
        }
        if (dto.getGuest() != null) {
            guest = JsonUtil.toJson(dto.getGuest());
        }
        if (dto.getTag() != null) {
            tag = JsonUtil.toJson(dto.getTag());
        }
    }

    @Override
    public VideoDto toDto() {
        return VideoDto.builder()
                .videoId(videoId)
                .title(title)
                .date(date)
                .duration(duration)
                .member(JsonUtil.fromJsonList(member, String.class))
                .guest(JsonUtil.fromJsonList(guest, String.class))
                .tag(JsonUtil.fromJsonList(tag, String.class))
                .description(description)
                .thumbnail(thumbnail)
                .views(views)
                .vLiveLink(vLiveLink)
                .build();
    }
}

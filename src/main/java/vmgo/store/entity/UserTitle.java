package vmgo.store.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;
import vmgo.domain.dto.TitleDto;
import vmgo.domain.dto.UserTitleDto;
import vmgo.store.entity.common.BaseTimeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table
public class UserTitle extends BaseTimeEntity<UserTitleDto> {
    @Id @Column(name = "user_title_id", length = 100)
    @GeneratedValue(generator = "user_title_uuid")
    @GenericGenerator(name = "user_title_uuid", strategy = "uuid2")
    private String userTitleId;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "title_id")
    private Title title;

    public UserTitle(){

    }

    public UserTitle(UserTitleDto userTitleDto) {
        super(userTitleDto);
    }

    @Override
    public void update(UserTitleDto dto) {
        BeanUtils.copyProperties(dto, this);

        if (dto.getTitle() != null) {
            Title title = new Title(dto.getTitle());
            this.title = title;
        }
        if (dto.getUser() != null) {
            User user = new User(dto.getUser());
            this.user = user;
        }
    }

    @Override
    public UserTitleDto toDto() {
        return UserTitleDto.builder()
                .userTitleId(userTitleId)
                .user(user.toDto())
                .title(title.toDto())
                .build();
    }

    public TitleDto toTitleListDto() {
        TitleDto titleDto = TitleDto.builder()
                .titleId(title.getTitleId())
                .titleName(title.getTitleName())
                .challengeId(title.getChallengeId())
                .build();

        titleDto.setCreated(getCreated());
        titleDto.setUpdated(getUpdated());
        return titleDto;
    }
}

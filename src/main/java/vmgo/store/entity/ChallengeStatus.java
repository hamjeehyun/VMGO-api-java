package vmgo.store.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;
import vmgo.domain.dto.ChallengeDto;
import vmgo.domain.dto.ChallengeStatusDto;
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
public class ChallengeStatus extends BaseTimeEntity<ChallengeStatusDto> {
    @Id @Column(name = "challenge_status_id", length = 100)
    @GeneratedValue(generator = "challenge_status_uuid")
    @GenericGenerator(name = "challenge_status_uuid", strategy = "uuid2")
    private String challengeStatusId;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;

    private boolean status;

    public ChallengeStatus() {
        super();
    }

    public ChallengeStatus(ChallengeStatusDto challengeStatusDto) {
        super(challengeStatusDto);
    }

    @Override
    public void update(ChallengeStatusDto dto) {
        BeanUtils.copyProperties(dto, this);

        if (dto.getChallenge() != null) {
            Challenge challenge = new Challenge(dto.getChallenge());
            this.challenge = challenge;
        }
        if (dto.getUser() != null) {
            User user = new User(dto.getUser());
            this.user = user;
        }
    }

    @Override
    public ChallengeStatusDto toDto() {
        return ChallengeStatusDto.builder()
                .challengeStatusId(challengeStatusId)
                .status(status)
                .challenge(challenge.toListDto())
                .user(user.toDto())
                .build();
    }
}

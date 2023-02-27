package vmgo.service;

import vmgo.domain.common.CommonResponseDto;

public interface VideoStatusService {
    CommonResponseDto updateVideoStatusComplete(String uid, String challengeId, String videoId);
}

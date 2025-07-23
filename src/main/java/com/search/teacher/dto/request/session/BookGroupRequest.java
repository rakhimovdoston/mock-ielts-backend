package com.search.teacher.dto.request.session;

import java.util.List;

public record BookGroupRequest(
        Long packageId,
        Long userId,
        Long branchId,
        List<Long> sessionIds,
        List<Long> speakingSessionIds
) {
}

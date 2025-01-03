package com.search.teacher.dto.modules.listening;

import com.search.teacher.dto.modules.PassageConfirmDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListeningUpdateRequest extends PassageConfirmDto {
    private String listeningPart;
}

package com.search.teacher.Techlearner.dto.question;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientAnswer {
    @NotNull
    private Long questionId;
    @NotNull
    private Long answerId;
}

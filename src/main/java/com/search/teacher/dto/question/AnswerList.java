package com.search.teacher.dto.question;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnswerList {
    private List<ClientAnswer> questions;
}

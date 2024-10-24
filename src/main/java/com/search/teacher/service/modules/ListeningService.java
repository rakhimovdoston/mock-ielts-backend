package com.search.teacher.service.modules;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.modules.ListeningAnswerDto;
import com.search.teacher.dto.modules.ListeningDto;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;

/**
 * Package com.search.teacher.service.modules
 * Created by doston.rakhimov
 * Date: 23/10/24
 * Time: 14:59
 **/
public interface ListeningService {
    JResponse saveListening(User currentUser, ListeningDto dto);

    JResponse saveQuestionListening(User currentUser, Long listeningId, ListeningAnswerDto dto);

    JResponse getAllListening(User currentUser, ModuleFilter filter);

    JResponse deleteListening(User currentUser, Long byId);
}

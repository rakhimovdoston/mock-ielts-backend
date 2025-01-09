package com.search.teacher.service.modules;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.modules.ReadingDto;
import com.search.teacher.dto.modules.ReadingPassageDto;
import com.search.teacher.dto.modules.listening.CheckListeningRequest;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.enums.Difficulty;
import com.search.teacher.model.enums.ModuleType;
import com.search.teacher.model.response.JResponse;

/**
 * Package com.search.teacher.service.modules
 * Created by doston.rakhimov
 * Date: 21/06/24
 * Time: 15:07
 **/
public interface ModuleService {

    JResponse getQuestionTypes(ModuleType type);

    JResponse checkListening(User currentUser, CheckListeningRequest request);

    JResponse getReading(User user, Difficulty difficulty);

    JResponse getListening(Integer moduleId, Difficulty difficulty);
}

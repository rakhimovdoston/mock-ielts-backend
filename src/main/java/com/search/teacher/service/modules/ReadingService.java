package com.search.teacher.service.modules;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.modules.PassageConfirmDto;
import com.search.teacher.dto.modules.RQuestionAnswerDto;
import com.search.teacher.dto.modules.ReadingPassageDto;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;

import java.util.List;

/**
 * Package com.search.teacher.service.modules
 * Created by doston.rakhimov
 * Date: 14/10/24
 * Time: 18:20
 **/
public interface ReadingService {
    JResponse createPassage(User currentUser, ReadingPassageDto passage);

    JResponse getReadingById(User currentUser, Long id, boolean withAnswer);

    JResponse saveReadingAnswer(User currentUser, Long questionId, RQuestionAnswerDto answer);

    JResponse updateReadingAnswer(User currentUser, Long readingId, RQuestionAnswerDto answer);

    JResponse updatePassage(User currentUser, ReadingPassageDto passage);

    JResponse deleteReadingPassage(User currentUser, Long readingId);

    JResponse deleteReadingAnswer(User currentUser, Long passageId, Long questionId, String type);

    JResponse getAllReadingPassage(User currentUser, ModuleFilter moduleFilter);

    JResponse getPassageQuestion(User currentUser, Long passageId);

    JResponse confirmPassage(User currentUser, PassageConfirmDto confirm);
}

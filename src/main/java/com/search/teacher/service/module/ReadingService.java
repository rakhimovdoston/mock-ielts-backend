package com.search.teacher.service.module;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.request.module.DeleteQuestionRequest;
import com.search.teacher.dto.request.module.ModuleAnswersRequest;
import com.search.teacher.dto.request.module.QuestionRequest;
import com.search.teacher.dto.request.module.ReadingPassageRequest;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;

public interface ReadingService {
    JResponse createPassage(User currentUser, ReadingPassageRequest passage);

    JResponse getPassage(User currentUser, Long readingId);

    JResponse getPassageQuestion(User currentUser, Long readingId);

    JResponse savePassageQuestion(User currentUser, QuestionRequest passage, Long readingId);

    JResponse saveModuleAnswers(User currentUser, ModuleAnswersRequest answers);

    JResponse getAllPassage(User currentUser, ModuleFilter filter);

    int countReadings(User currentUser);

    JResponse deleteListening(User currentUser, Long listeningId);

    JResponse deleteListeningQuestion(User currentUser, Long readingId, DeleteQuestionRequest request);

    JResponse updateListening(User currentUser, Long readingId, DeleteQuestionRequest request);

    JResponse updateReadingPassage(User currentUser, Long readingId, ReadingPassageRequest passage);
}

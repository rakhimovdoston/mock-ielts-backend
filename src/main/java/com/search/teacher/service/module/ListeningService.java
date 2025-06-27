package com.search.teacher.service.module;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.request.module.DeleteQuestionRequest;
import com.search.teacher.dto.request.module.ListeningRequest;
import com.search.teacher.dto.request.module.ModuleAnswersRequest;
import com.search.teacher.dto.request.module.QuestionRequest;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;

public interface ListeningService {
    JResponse createListening(User currentUser, ListeningRequest request);

    JResponse getListening(User currentUser, Long id);

    JResponse getAllListening(User currentUser, ModuleFilter filter);

    JResponse saveListeningQuestion(User currentUser, Long listeningId, QuestionRequest request);

    JResponse getListeningQuestionByListening(User currentUser, Long listeningId);

    JResponse saveModuleAnswers(User currentUser, ModuleAnswersRequest answers);

    int countListening(User currentUser);

    JResponse deleteListening(User currentUser, Long listeningId);

    JResponse deleteListeningQuestion(User currentUser, Long listeningId, DeleteQuestionRequest request);

    JResponse updateListening(User currentUser, Long listeningId, DeleteQuestionRequest request);
}

package com.search.teacher.service.modules;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.modules.ListeningAnswerDto;
import com.search.teacher.dto.modules.ListeningDto;
import com.search.teacher.dto.modules.PassageConfirmDto;
import com.search.teacher.dto.modules.listening.ListeningResponse;
import com.search.teacher.model.entities.Image;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * Package com.search.teacher.service.modules
 * Created by doston.rakhimov
 * Date: 23/10/24
 * Time: 14:59
 **/
public interface ListeningService {

    JResponse saveQuestionListening(User currentUser, Long listeningId, ListeningAnswerDto dto);

    JResponse getAllListening(User currentUser, ModuleFilter filter);

    JResponse deleteListening(User currentUser, Long byId);

    JResponse getListeningById(User currentUser, Long id);

    ListeningResponse createListening(Organization organization, Image image, String listeningPart);

    JResponse uploadAudio(User currentUser, MultipartFile file, String listeningPart);

    JResponse deleteListeningQuestion(User currentUser, Long listeningId, Long questionId, String type);

    JResponse confirmListening(User currentUser, PassageConfirmDto confirm);

    JResponse getAnswerListening(User currentUser, Long byId);
}

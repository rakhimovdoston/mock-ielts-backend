package com.search.teacher.service.module;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.request.module.WritingRequest;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;
import org.springframework.web.multipart.MultipartFile;

public interface WritingService {
    JResponse saveWriting(User currentUser, WritingRequest request);

    JResponse getAllWritings(User currentUser, ModuleFilter filter);

    int countWritings(User currentUser);

    JResponse deleteWriting(User currentUser, Long listeningId);

    JResponse getWritingById(User currentUser, Long id);

    JResponse updateWriting(User currentUser, Long byId, WritingRequest request);

    JResponse deleteAudioByUrl(User currentUser, String url, Long writingId);

    JResponse uploadPhoto(User currentUser, Long byId,  MultipartFile file, String type);
}

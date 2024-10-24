package com.search.teacher.service.modules;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.modules.writing.WritingDto;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.utils.SecurityUtils;
import jakarta.validation.Valid;

/**
 * Package com.search.teacher.service.modules
 * Created by doston.rakhimov
 * Date: 22/10/24
 * Time: 17:50
 **/
public interface WritingService {
    JResponse saveWritingContent(User currentUser, @Valid WritingDto writingDto);

    JResponse updateWriting(User currentUser, @Valid WritingDto writingDto);

    JResponse getWritingModule(User currentUser, Long id);

    JResponse deleteWritingModule(User currentUser, Long byId);

    JResponse getAllWriting(User currentUser, ModuleFilter filter);
}

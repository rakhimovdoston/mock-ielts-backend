package com.search.teacher.service.modules;

import com.search.teacher.dto.modules.ReadingDto;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;

/**
 * Package com.search.teacher.service.modules
 * Created by doston.rakhimov
 * Date: 21/06/24
 * Time: 15:07
 **/
public interface ModuleService {
    JResponse saveReading(User user, ReadingDto reading);
}

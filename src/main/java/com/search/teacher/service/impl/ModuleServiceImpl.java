package com.search.teacher.service.impl;

import com.search.teacher.dto.modules.ReadingDto;
import com.search.teacher.dto.response.SaveResponse;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.entities.modules.reading.ReadingPassage;
import com.search.teacher.model.response.JResponse;
//import com.search.teacher.repository.modules.ListeningRepository;
import com.search.teacher.repository.modules.ReadingRepository;
import com.search.teacher.repository.modules.SpeakingRepository;
import com.search.teacher.repository.modules.WritingRepository;
import com.search.teacher.service.modules.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Package com.search.teacher.service.impl
 * Created by doston.rakhimov
 * Date: 21/06/24
 * Time: 15:07
 **/
@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final SpeakingRepository speakingRepository;
    private final ReadingRepository readingRepository;
//    private final ListeningRepository listeningRepository;
    private final WritingRepository writingRepository;

    @Override
    public JResponse saveReading(User user, ReadingDto dto) {
        ReadingPassage reading = new ReadingPassage();
        readingRepository.save(reading);
        return JResponse.success(new SaveResponse(reading.getId()));
    }
}

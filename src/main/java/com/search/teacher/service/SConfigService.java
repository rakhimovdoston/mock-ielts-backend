package com.search.teacher.service;

import com.search.teacher.model.entities.SConfig;
import com.search.teacher.repository.SConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SConfigService {
    private final SConfigRepository sConfigRepository;

    public boolean checkService(String key) {
        SConfig sConfig = sConfigRepository.findByConfigKey(key);
        return sConfig == null || sConfig.isActive();
    }

    public String getValue(String key) {
        SConfig sConfig = sConfigRepository.findByConfigKey(key);
        if (sConfig == null) {
            return null;
        }
        return sConfig.getValue();
    }
}

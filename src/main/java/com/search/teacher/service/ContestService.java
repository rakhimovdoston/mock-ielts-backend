package com.search.teacher.service;

import com.search.teacher.repository.ContestRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Package com.search.teacher.service
 * Created by doston.rakhimov
 * Date: 23/10/24
 * Time: 12:24
 **/
@Service
@RequiredArgsConstructor
public class ContestService {
    private final Logger logger = LoggerFactory.getLogger(ContestService.class);
    private final ContestRepository contestRepository;
}

package com.search.teacher.controller.modules;

import com.search.teacher.service.ContestService;
import com.search.teacher.utils.SecurityUtils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Package com.search.teacher.controller.modules
 * Created by doston.rakhimov
 * Date: 22/10/24
 * Time: 11:48
 **/
@RestController
@RequestMapping("api/v1/contest")
@Api(tags = "Contest API")
@RequiredArgsConstructor
public class ContestController {

    private final ContestService contestService;
    private final SecurityUtils securityUtils;
}

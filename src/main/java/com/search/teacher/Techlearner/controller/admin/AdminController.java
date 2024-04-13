package com.search.teacher.Techlearner.controller.admin;

import com.search.teacher.Techlearner.model.response.JResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/admin")
@Tag(name = "Admin Controller")
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CacheManager cacheManager;

    public AdminController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @GetMapping("clear-redis-cache")
    public JResponse clearCache(@RequestParam(name = "cacheName", required = false, defaultValue = "") String cacheName) {
        if (StringUtils.hasText(cacheName)) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache == null)
                return JResponse.error(400, "This name is unavailable in the cache");
            cache.clear();
            logger.info("'{}' cache cleared successfully.", cacheName);
        } else {
            for (String name : cacheManager.getCacheNames()) {
                Cache cache = cacheManager.getCache(name);
                if (cache != null) {
                    cache.clear();
                    logger.info("'{}' name cache cleared successfully", name);
                }
            }
        }
        return JResponse.success();
    }
}

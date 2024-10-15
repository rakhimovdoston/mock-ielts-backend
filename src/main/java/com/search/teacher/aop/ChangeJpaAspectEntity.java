package com.search.teacher.aop;

import com.search.teacher.components.Constants;
import com.search.teacher.model.entities.User;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ChangeJpaAspectEntity {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CacheManager cacheManager;

    @AfterReturning(pointcut = "execution(* com.search.teacher.repository.UserRepository.save(..))", returning = "user")
    public void clearUserFromCache(Object user) {
        if (user instanceof User) {
            Cache cache = cacheManager.getCache(Constants.USER_EMAIL);
            if (cache != null) {
                cache.clear();
                logger.info("Clearing cache by user email");
            }
        }
    }
}

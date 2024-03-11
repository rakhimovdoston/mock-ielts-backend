package com.search.teacher.Techlearner.aop;

import com.search.teacher.Techlearner.components.Constants;
import com.search.teacher.Techlearner.model.entities.Question;
import com.search.teacher.Techlearner.model.entities.User;
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

    @AfterReturning(pointcut = "execution(* com.search.teacher.Techlearner.repository.UserRepository.save(..))", returning = "user")
    public void clearUserFromCache(Object user) {
        if (user instanceof User) {
            Cache cache = cacheManager.getCache(Constants.USER_EMAIL);
            if (cache != null) {
                cache.clear();
                logger.info("Clearing cache by user email");
            }
        }
    }

    @AfterReturning(pointcut = "execution(* com.search.teacher.Techlearner.repository.QuestionRepository.save(..)))", returning = "question")
    public void clearQuestionCache(Object question) {
        if (question instanceof Question) {
            Cache cache = cacheManager.getCache(Constants.QUESTION_CACHE);
            if (cache != null) {
                cache.clear();
                logger.info("Clearing cache by question");
            }
        }
    }
}

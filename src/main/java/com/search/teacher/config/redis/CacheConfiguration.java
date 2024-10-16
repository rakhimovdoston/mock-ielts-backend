//package com.search.teacher.config.redis;
//
//import com.search.teacher.components.Constants;
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.redisson.spring.cache.CacheConfig;
//import org.redisson.spring.cache.RedissonSpringCacheManager;
//import org.redisson.spring.data.connection.RedissonConnectionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//
//import java.io.IOException;
//import java.time.Duration;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.Stream;
//
//@Configuration
//@EnableCaching
//public class CacheConfiguration {
//
//    @Autowired
//    private Environment environment;
//
//    @Autowired
//    private ResourceLoader resourceLoader;
//    static final long CACHE_30_MINUTES = 30;
//    static final long CACHE_1_HOUR = 1;
//    static final long CACHE_1_DAY = 1;
//    static final long CACHE_15_DAYS = 15;
//
//    @Bean(destroyMethod = "shutdown")
//    public RedissonClient redissonClient() throws IOException {
//        String filename = "classpath:redis/".concat(getActiveProfile()).concat("/redis.yml");
//        Resource resource = resourceLoader.getResource(filename);
//        Config config = Config.fromYAML(resource.getInputStream());
//        return Redisson.create(config);
//    }
//
//    @Bean
//    public RedisConnectionFactory redissonConnectionFactory(RedissonClient redissonClient) {
//        return new RedissonConnectionFactory(redissonClient);
//    }
//
//    @Bean
//    public CacheManager cacheManager(RedissonClient redissonClient) {
//        Map<String, CacheConfig> config = new HashMap<>();
//        config.put(Constants.USER_EMAIL, new CacheConfig(Duration.ofHours(CACHE_1_HOUR).toMillis(), 0));
//        return new RedissonSpringCacheManager(redissonClient, config);
//    }
//
//    private String getActiveProfile() {
//        return Stream.of(environment.getActiveProfiles()).findFirst().orElse("local");
//    }
//}

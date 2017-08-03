package gov.va.ascent.demo.service.config;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableCaching
@ConditionalOnProperty(name = "redis.enabled", havingValue = "true", matchIfMissing = false)
public class DemoCacheConfig extends CachingConfigurerSupport {
  
  final static Logger LOGGER = LoggerFactory.getLogger(DemoCacheConfig.class);
  
  @Autowired
  private DemoCacheExpiresConfig demoCacheExpiresConfig;

  @Bean
  public DemoCacheRedisTemplate<String, Object> redisTemplate(JedisConnectionFactory connectionFactory) {
      final DemoCacheRedisTemplate<String, Object> redisTemplate = new DemoCacheRedisTemplate<String, Object>();
      setSerializer(redisTemplate);
      redisTemplate.setConnectionFactory(connectionFactory);
      LOGGER.info("Host Name: {}", connectionFactory.getHostName());
      LOGGER.info("Port: {}", connectionFactory.getPort());
      LOGGER.info("Timeout: {}", connectionFactory.getTimeout());

      return redisTemplate;
  }
 
  @Bean
  public CacheManager cacheManager(DemoCacheRedisTemplate<String, Object> redisTemplate) {
    RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
    redisCacheManager.setLoadRemoteCachesOnStartup(true);
    redisCacheManager.setUsePrefix(true);
    
    demoCacheExpiresConfig.getExpires().forEach((k,v)->LOGGER.info("Cache Name: " + k + " TTL: " + v));
    LOGGER.info("Default Expires: {}",  demoCacheExpiresConfig.getDefaultExpires());
    redisCacheManager.setDefaultExpiration(demoCacheExpiresConfig.getDefaultExpires());
    redisCacheManager.setExpires(demoCacheExpiresConfig.getExpires());
    return redisCacheManager;
  }
  
  /**
   * 
   */
  private void setSerializer(DemoCacheRedisTemplate<String, Object> redisTemplate){
      
      redisTemplate.setKeySerializer(new Jackson2JsonRedisSerializer<>(Object.class));

      Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
      ObjectMapper om = new ObjectMapper();
      om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
      om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
      jackson2JsonRedisSerializer.setObjectMapper(om);
      redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
      redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
  }
  
  @Bean
  public KeyGenerator customKeyGenerator() {
    return new KeyGenerator() {
      
      @Override
      public Object generate(Object o, Method method, Object... objects) {
        LOGGER.info("Generating cacheKey ");
        StringBuilder sb = new StringBuilder();
        sb.append(o.getClass().getName());
        sb.append(method.getName());
        for (Object obj : objects) {
          sb.append(obj.toString());
        }
        LOGGER.info("Generated cacheKey {}", sb.toString());
        return sb.toString();
      }
    };
  }
 

  @Override
  public CacheErrorHandler errorHandler() {
      return new RelaxedCacheErrorHandler();
  }
  
  private static class RelaxedCacheErrorHandler extends SimpleCacheErrorHandler {
    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        LOGGER.error("Error getting from cache.", exception);
    }
  }
  
}
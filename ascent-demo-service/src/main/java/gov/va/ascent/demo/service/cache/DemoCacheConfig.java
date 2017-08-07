package gov.va.ascent.demo.service.cache;

import java.lang.reflect.Method;
import java.util.Iterator;

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
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
      if (connectionFactory.getSentinelConnection() != null) {
          if(!connectionFactory.getSentinelConnection().masters().isEmpty()) {
            final Iterator<?> iSentinel = connectionFactory.getSentinelConnection().masters().iterator();
            while (iSentinel.hasNext()) {
              final RedisServer redisServer = (RedisServer)iSentinel.next();
                LOGGER.info("Redis Master Server Name: {}", redisServer.getName());
                LOGGER.info("Redis Master Server Port: {}", redisServer.getPort());
            }
          } else {
            LOGGER.warn("No Master Server Found, Check the Configurations for redis");
          }
      } else {
        LOGGER.info("Host Name: {}", connectionFactory.getHostName());
        LOGGER.info("Port: {}", connectionFactory.getPort());
        LOGGER.info("Timeout: {}", connectionFactory.getTimeout());
      }

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
      
      GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(createRedisObjectmapper());
    
      redisTemplate.setKeySerializer(new GenericJackson2JsonRedisSerializer());
      redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
      redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
  }
  
  private static ObjectMapper createRedisObjectmapper() {
    return new ObjectMapper()
            .enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL,JsonTypeInfo.As.PROPERTY)//\\
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            .configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, true)
            .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
            .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
            .configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false)
            .configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false) //\\
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
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
        LOGGER.error("Error getting from cache. Cache Name: {} Key: {} Message: {}", cache.getName(), key, exception.getMessage());
    }
  }
  
}
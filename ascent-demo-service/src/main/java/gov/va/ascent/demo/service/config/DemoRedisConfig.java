package gov.va.ascent.demo.service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;

//import com.baeldung.spring.data.redis.queue.MessagePublisher;
//import com.baeldung.spring.data.redis.queue.RedisMessagePublisher;
//import com.baeldung.spring.data.redis.queue.RedisMessageSubscriber;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
@ComponentScan("gov.va.ascent.demo.service")
@ConditionalOnProperty(name = "redis.enabled", havingValue = "true", matchIfMissing = false)
public class DemoRedisConfig extends CachingConfigurerSupport {
  
  final static Logger LOGGER = LoggerFactory.getLogger(DemoRedisConfig.class);

  @Bean
  public RedisConnectionFactory jedisConnectionFactory() {
      JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
      return jedisConnectionFactory;
  }

  @Bean
  public DemoRedisTemplate<String, Object> redisTemplate() {
      final DemoRedisTemplate<String, Object> template = new DemoRedisTemplate<String, Object>();
      template.setConnectionFactory(jedisConnectionFactory());
      return template;
  }

    
  @Bean
  public CacheManager cacheManager() {
      return new RedisCacheManager(redisTemplate());
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
  
  /**
  @Bean
  MessageListenerAdapter messageListener() {
      return new MessageListenerAdapter(new RedisMessageSubscriber());
  }
  
  @Bean
  RedisMessageListenerContainer redisContainer() {
      final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
      container.setConnectionFactory(jedisConnectionFactory());
      container.addMessageListener(messageListener(), topic());
      return container;
  }

 // @Bean
 // MessagePublisher redisPublisher() {
  //    return new RedisMessagePublisher(redisTemplate(), topic());
  //}

  //@Bean
  //ChannelTopic topic() {
   //   return new ChannelTopic("pubsub:queue");
  //}
   * **/
}
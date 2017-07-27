package gov.va.ascent.demo.service.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;


/**
 * An extension of RedisTemplate that logs exceptions instead of letting them propagate.
 * If the Redis server is unavailable, cache operations are always a "miss" and data is fetched from the database.
 */
public class DemoRedisTemplate<K, V> extends RedisTemplate<K, V> {

  final static Logger LOGGER = LoggerFactory.getLogger(DemoRedisTemplate.class);


    @Override
    public <T> T execute(final RedisCallback<T> action, final boolean exposeConnection, final boolean pipeline) {
        try {
            return super.execute(action, exposeConnection, pipeline);
        }
        catch(final Throwable t) {
          LOGGER.warn("Error executing cache operation: {}", t.getMessage());
            return null;
        }
    }


    @Override
    public <T> T execute(final RedisScript<T> script, final List<K> keys, final Object... args) {
        try {
            return super.execute(script, keys, args);
        }
        catch(final Throwable t) {
            LOGGER.warn("Error executing cache operation: {}", t.getMessage());
            return null;
        }
    }


    @Override
    public <T> T execute(final RedisScript<T> script, final RedisSerializer<?> argsSerializer, final RedisSerializer<T> resultSerializer, final List<K> keys, final Object... args) {
        try {
            return super.execute(script, argsSerializer, resultSerializer, keys, args);
        }
        catch(final Throwable t) {
            LOGGER.warn("Error executing cache operation: {}", t.getMessage());
            return null;
        }
    }


    @Override
    public <T> T execute(final SessionCallback<T> session) {
        try {
            return super.execute(session);
        }
        catch(final Throwable t) {
            LOGGER.warn("Error executing cache operation: {}", t.getMessage());
            return null;
        }
    }
}

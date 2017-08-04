package gov.va.ascent.demo.service.cache;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="redis.cache")
public class DemoCacheExpiresConfig {
  
    final static Logger LOGGER = LoggerFactory.getLogger(DemoCacheExpiresConfig.class);
   
    private Map<String, Long> expires;
    
    @Value("${redis.cache.defaultExpires:86400}")
    private Long defaultExpires;
    
    public DemoCacheExpiresConfig() {
      this.expires = new HashMap<String, Long>();
    }

    public Map<String, Long> getExpires() {
        return this.expires;
    }
    
    public Long getDefaultExpires() {
      return this.defaultExpires;
  }
    
}

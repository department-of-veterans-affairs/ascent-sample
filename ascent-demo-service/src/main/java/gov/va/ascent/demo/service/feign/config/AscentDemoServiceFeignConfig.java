package gov.va.ascent.demo.service.feign.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

import feign.Feign;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;
import gov.va.ascent.demo.service.utils.HystrixCommandConstants;

@Configuration
public class AscentDemoServiceFeignConfig {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(AscentDemoServiceFeignConfig.class);
	
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	@ConditionalOnProperty(name = "feign.hystrix.enabled", matchIfMissing = true)
	public Feign.Builder feignBuilder() {
	  SetterFactory commandKeyIsRequestLine = (target, method) -> {
	    String groupKey = HystrixCommandConstants.ASCENT_DEMO_SERVICE_GROUP_KEY;
		String commandKey =  Feign.configKey(target.type(), method);
	    LOGGER.debug("Feign Hystrix Group Key: {}", groupKey);
	    LOGGER.debug("Feign Hystrix Command Key: {}", commandKey);
	    return HystrixCommand.Setter
	      .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
	      .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey));
	  };
	  return HystrixFeign.builder().setterFactory(commandKeyIsRequestLine);
	}
}

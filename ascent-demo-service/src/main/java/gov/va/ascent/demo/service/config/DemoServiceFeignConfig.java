package gov.va.ascent.demo.service.config;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;
import gov.va.ascent.demo.service.utils.HystrixCommandConstants;
import gov.va.ascent.security.jwt.JwtTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Configuration
public class DemoServiceFeignConfig {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DemoServiceFeignConfig.class);

	@Autowired
	@Qualifier("tokenFeignRequestInterceptor")
	private RequestInterceptor feignRequestInterceptor;
	
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
	  return HystrixFeign.builder().setterFactory(commandKeyIsRequestLine).requestInterceptor(feignRequestInterceptor);
	}

}

@Component
class TokenFeignRequestInterceptor implements RequestInterceptor {

	private final static Logger LOGGER = LoggerFactory.getLogger(TokenFeignRequestInterceptor.class);

	@Autowired
	private JwtTokenService tokenService;

	public void apply(RequestTemplate template) {
		Map<String, String> tokenMap =  tokenService.getTokenFromRequest();
		for(String token: tokenMap.keySet()){
			LOGGER.info("Adding Token Header {} {}", token, tokenMap.get(token));
			template.header(token, tokenMap.get(token));
		}
	}
}
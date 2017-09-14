package gov.va.ascent.demo.service.config;

import org.springframework.context.annotation.Configuration;
import gov.va.ascent.demo.service.utils.HystrixCommandConstants;
import gov.va.ascent.starter.feign.autoconfigure.AscentFeignAutoConfiguration;;

@Configuration
public class DemoServiceFeignConfig extends AscentFeignAutoConfiguration{

    public DemoServiceFeignConfig() {
    		super.setGroupKey(HystrixCommandConstants.ASCENT_DEMO_SERVICE_GROUP_KEY); 
    }	
    
}

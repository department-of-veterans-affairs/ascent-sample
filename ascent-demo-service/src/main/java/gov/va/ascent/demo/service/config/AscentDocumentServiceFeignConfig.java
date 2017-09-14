package gov.va.ascent.demo.service.config;

import org.springframework.context.annotation.Configuration;
import gov.va.ascent.demo.service.utils.HystrixCommandConstants;
import gov.va.ascent.starter.feign.autoconfigure.AscentFeignAutoConfiguration;

@Configuration
public class AscentDocumentServiceFeignConfig  extends AscentFeignAutoConfiguration{
	
    public AscentDocumentServiceFeignConfig() {
        super.setGroupKey(HystrixCommandConstants.ASCENT_DOCUMENT_SERVICE_GROUP_KEY); 
    }
    
}


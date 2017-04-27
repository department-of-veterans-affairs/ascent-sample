package gov.va.ascent.demo.service.rest.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gov.va.ascent.demo.service.feign.config.AscentDemoServiceFeignConfig;
import gov.va.ascent.demo.service.api.v1.transfer.EchoHostServiceResponse;

@FeignClient(value="ascent-demo-service", 
				fallback = FeignEchoClientFallback.class, 
					configuration = AscentDemoServiceFeignConfig.class)
public interface FeignEchoClient{
	
	@RequestMapping(value = "/demo/v1/echo", method = RequestMethod.GET)
	ResponseEntity<EchoHostServiceResponse> echo();
	
}

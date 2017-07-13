package gov.va.ascent.demo.service.rest.client.restTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import gov.va.ascent.demo.service.api.v1.transfer.EchoHostServiceResponse;

public class DemoUsageRestTemplate {
	@Autowired
    private RestTemplate restTemplate;	
	
	public ResponseEntity<EchoHostServiceResponse> invokeServiceUsingRestTemplate() {
		ResponseEntity<EchoHostServiceResponse> exchange =
                this.restTemplate.exchange(
                        "http://ascent-demo-service/demo/v1/echo",
                        HttpMethod.GET, null, 
                        new ParameterizedTypeReference<EchoHostServiceResponse>(){});
		return exchange;
	}

}

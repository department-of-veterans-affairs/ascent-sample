package gov.va.ascent.demo.service.rest.client;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import gov.va.ascent.demo.service.api.v1.transfer.EchoHostServiceResponse;
import gov.va.ascent.demo.service.api.v1.transfer.ServiceInstanceDetail;
import gov.va.ascent.demo.service.api.v1.transfer.ServiceInstancesServiceResponse;
import gov.va.ascent.demo.service.rest.provider.DemoServiceEndpoint;
import gov.va.ascent.framework.swagger.SwaggerResponseMessages;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The purpose of this class is to make REST client calls.  These are REST clients to our own
 * services just to experiment with how to use REST clients through the various techniques.
 * 
 * @author jshrader
 */
@RestController
public class DemoServiceRestClientTests implements SwaggerResponseMessages {

	private final static Logger LOGGER = LoggerFactory.getLogger(DemoServiceRestClientTests.class);
	
	@Autowired
    private DiscoveryClient discoveryClient;
	
	@Autowired
    private RestTemplate restTemplate;
	
	@Autowired
    private FeignEchoClient feignEchoClient;
	
	public static final String URL_PREFIX = DemoServiceEndpoint.URL_PREFIX + "/clientTests";
	
	@ApiOperation(value = "An endpoint demo's using the DiscoveryClient to interrogate services.")
	@RequestMapping(value = URL_PREFIX + "/demoDiscoveryClientUsage", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = Health.class, message = MESSAGE_200),
			@ApiResponse(code = 500, response = Health.class, message = MESSAGE_500),
			@ApiResponse(code = 403, message = MESSAGE_403) })
    public ResponseEntity<ServiceInstancesServiceResponse> demoDiscoveryClientUsage(HttpServletRequest request) {
		ServiceInstancesServiceResponse response = new ServiceInstancesServiceResponse();
		
		discoveryClient.getServices().forEach((String service) -> {
			//use discovery service to build out a collection of our ServiceInstanceDetail objects
			discoveryClient.getInstances(service).forEach((ServiceInstance serviceInstance) -> {
				ServiceInstanceDetail serviceInstanceDetail = new ServiceInstanceDetail();
				serviceInstanceDetail.setHost(serviceInstance.getHost());
				serviceInstanceDetail.setPort(Integer.toString(serviceInstance.getPort()));
				serviceInstanceDetail.setUri(serviceInstance.getUri().toString());
				serviceInstanceDetail.setServiceId(serviceInstance.getServiceId());
				serviceInstanceDetail.setMetaData(Arrays.toString(serviceInstance.getMetadata().entrySet().toArray()));
				response.getServiceInstanceDetail().add(serviceInstanceDetail);
			});
		});
		
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	@ApiOperation(value = "An endpoint which uses a REST client using RestTemplate to call the remote echo operation.")
	@RequestMapping(value = URL_PREFIX + "/demoCallEchoUsingRestTemplate", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = Health.class, message = MESSAGE_200),
			@ApiResponse(code = 500, response = Health.class, message = MESSAGE_500),
			@ApiResponse(code = 403, message = MESSAGE_403) })
    public ResponseEntity<EchoHostServiceResponse> demoCallEchoUsingRestTemplate(HttpServletRequest request) {
		//invoke the service using classic REST Template from Spring, but load balanced through Eureka/Zuul
		ResponseEntity<EchoHostServiceResponse> exchange =
                this.restTemplate.exchange(
                        "http://ascent-demo-service/demo/v1/echo",
                        HttpMethod.GET, null, 
                        new ParameterizedTypeReference<EchoHostServiceResponse>(){});
		LOGGER.info("INVOKED A ASCENT-DEMO-SERVICE USING REST TEMPLATE: " + exchange.getBody());
		
        return new ResponseEntity<EchoHostServiceResponse>(exchange.getBody(), exchange.getStatusCode());
    }
	
	@ApiOperation(value = "An endpoint which uses a REST client using Feign to call the remote echo operation.")
	@RequestMapping(value = URL_PREFIX + "/demoCallEchoUsingFeignClient", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = Health.class, message = MESSAGE_200),
			@ApiResponse(code = 500, response = Health.class, message = MESSAGE_500),
			@ApiResponse(code = 403, message = MESSAGE_403) })
    public ResponseEntity<EchoHostServiceResponse> demoCallEchoUsingFeignClient(HttpServletRequest request) {
		
		// use this in case of feign hystrix to test fallback handler invocation
	    //ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.circuitBreaker.forceOpen", "true");
	    
		ResponseEntity<EchoHostServiceResponse> echoResponse= feignEchoClient.echo();
		
		// use this in case of feign hystrix
	    //ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.circuitBreaker.forceOpen", "false");
		
		return echoResponse;
    }
	
}

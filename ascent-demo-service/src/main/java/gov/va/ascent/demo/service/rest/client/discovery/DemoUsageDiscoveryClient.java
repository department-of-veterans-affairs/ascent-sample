package gov.va.ascent.demo.service.rest.client.discovery;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import gov.va.ascent.demo.service.api.v1.transfer.ServiceInstanceDetail;
import gov.va.ascent.demo.service.api.v1.transfer.ServiceInstancesServiceResponse;

/**
 * REST Client class that uses DiscoveryClient api to invoke the service by iterating through
 * the service instances  
 * @author
 *
 */

public class DemoUsageDiscoveryClient {
	@Autowired
    private DiscoveryClient discoveryClient;
	
	public ServiceInstancesServiceResponse invokeServiceUsingDiscoveryClient() {
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
		return response;
	}
}

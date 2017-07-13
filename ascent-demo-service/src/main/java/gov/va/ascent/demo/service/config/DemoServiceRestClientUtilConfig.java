package gov.va.ascent.demo.service.config;


import org.springframework.context.annotation.Bean;

import gov.va.ascent.demo.service.rest.client.discovery.DemoUsageDiscoveryClient;
import gov.va.ascent.demo.service.rest.client.restTemplate.DemoUsageRestTemplate;

public class DemoServiceRestClientUtilConfig extends DemoServiceRestClientTestsConfig {

	@Bean
	DemoUsageDiscoveryClient demoUsageDiscoveryClient() {
		return new DemoUsageDiscoveryClient();
	}

	@Bean
	DemoUsageRestTemplate demoUsageRestTemplate() {
		return new DemoUsageRestTemplate();
	}
	
}

package gov.va.ascent.demo.service.config;

import gov.va.ascent.demo.service.rest.client.discovery.DemoUsageDiscoveryClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoServiceRestClientTestsConfig {
	
	@Bean
    DemoUsageDiscoveryClient demoUsageDiscoveryClient() {
        return new DemoUsageDiscoveryClient();
    }
	
}
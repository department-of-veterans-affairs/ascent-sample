package gov.va.ascent.demo.service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import gov.va.ascent.demo.service.rest.client.discovery.DemoUsageDiscoveryClient;
import gov.va.ascent.demo.service.rest.client.restTemplate.DemoUsageRestTemplate;

@Configuration
public class DemoServiceRestClientTestsConfig {
	
	@Bean
    @LoadBalanced
    RestTemplate restTemplate() {
         return new RestTemplate();
	}
	
	@Bean
    DemoUsageDiscoveryClient demoUsageDiscoveryClient() {
        return new DemoUsageDiscoveryClient();
    }

    @Bean
    DemoUsageRestTemplate demoUsageRestTemplate() {
        return new DemoUsageRestTemplate();
    }
	
}

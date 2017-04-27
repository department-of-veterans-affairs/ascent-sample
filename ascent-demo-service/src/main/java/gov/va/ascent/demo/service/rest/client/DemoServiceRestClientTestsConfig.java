package gov.va.ascent.demo.service.rest.client;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DemoServiceRestClientTestsConfig {
	
	@Bean
    @LoadBalanced
    RestTemplate restTemplate() {
         return new RestTemplate();
	}
	
}

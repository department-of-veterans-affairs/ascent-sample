package gov.va.ascent.demo.service.config;

import gov.va.ascent.demo.partner.person.ws.client.PersonWsClientConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DemoServiceRestClientTestsConfig.class, PersonWsClientConfig.class})
public class AscentDemoServiceConfig {

}

package gov.va.ascent.demo.service.config;

import gov.va.ascent.demo.partner.person.ws.client.PersonWsClientConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = { "gov.va.ascent.framework.service, gov.va.ascent.framework.rest.provider, gov.va.ascent.framework.audit" }, excludeFilters = @Filter(Configuration.class))
@Import({DemoServiceRestClientTestsConfig.class, PersonWsClientConfig.class})
public class AscentDemoServiceConfig {

}

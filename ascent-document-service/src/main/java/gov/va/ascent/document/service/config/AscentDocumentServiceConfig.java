package gov.va.ascent.document.service.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "gov.va.ascent.framework.service, gov.va.ascent.framework.rest.provider" }, excludeFilters = @Filter(Configuration.class))
public class AscentDocumentServiceConfig {

}

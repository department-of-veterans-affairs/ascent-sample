package gov.va.ascent.demo.service;

import static springfox.documentation.builders.PathSelectors.regex;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import gov.va.ascent.demo.partner.person.ws.client.PersonWsClientConfig;
import gov.va.ascent.demo.service.rest.client.DemoServiceRestClientTestsConfig;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@ComponentScan(basePackages = { "gov.va.ascent.framework.service, gov.va.ascent.framework.rest.provider" }, excludeFilters = @Filter(Configuration.class))
@Import({BeanValidatorPluginsConfiguration.class, DemoServiceRestClientTestsConfig.class, PersonWsClientConfig.class}) 
public class AscentDemoServiceConfig {
	
	
	//BEANS BELOW HERE FOR SWAGGER
    @Autowired
    Jackson2ObjectMapperBuilder objectMapperBuilder;
    
    @PostConstruct
    protected void postConstruct(){
		 //register the Jaxb module so swagger will read the jaxb annotations
		 objectMapperBuilder.modules(new JaxbAnnotationModule());
    }
    
    @Bean
    public Docket categoryApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("demo-v1")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/demo/v1.*"))
                .build()
                .ignoredParameterTypes(ApiIgnore.class)
                .enableUrlTemplating(true);
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Ascent Demo Service v1 API Documentation")
                .description("API Error Keys <a href=\"swagger/error-keys.html\">error keys</a>")
                .version("1.0")
                .build();
    }
}

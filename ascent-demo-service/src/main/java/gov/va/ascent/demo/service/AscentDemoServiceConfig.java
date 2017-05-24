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
import org.springframework.web.bind.annotation.RestController;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .build()
                .ignoredParameterTypes(ApiIgnore.class)
                .enableUrlTemplating(true)
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()));
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Ascent Demo Service v1 API Documentation")
                .description("API Error Keys <a href=\"swagger/error-keys.html\">error keys</a>")
                .version("1.0")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/demo.*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;

        List<SecurityReference> list = new ArrayList<>();
        list.add(new SecurityReference("Authorization", authorizationScopes));
        return list;
    }
//
//    @Bean
//    SecurityConfiguration security() {
//        return new SecurityConfiguration(
//                null,
//                null,
//                null,
//                null,
//                "BEARER jwt_token",
//                ApiKeyVehicle.HEADER,
//                "Authorization",
//                null);
//    }
}

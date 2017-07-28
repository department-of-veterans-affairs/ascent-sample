package gov.va.ascent.document.service.config;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

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

@Configuration
@ComponentScan(basePackages = { "gov.va.ascent.framework.service, gov.va.ascent.framework.rest.provider" }, excludeFilters = @Filter(Configuration.class))

public class AscentDocumentServiceConfig {
	
	
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
                .groupName("document-v1")
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
                .title("Ascent Document Service v1 API Documentation")
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
                .forPaths(PathSelectors.regex("/document.*"))
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
}

package gov.va.ascent.demo.service;

import gov.va.ascent.security.config.EnableAscentSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * An <tt>Ascent Demo Service Application</tt> enabled for Spring Boot Application, 
 * Spring Cloud Netflix Feign Clients, Hystrix circuit breakers, Swagger and 
 * AspectJ's @Aspect annotation.
 *
 */
@SpringBootApplication
@EnableSwagger2 //only needed due to swagger
@EnableDiscoveryClient //needed to reach out to spring cloud config, eureka
@EnableAspectJAutoProxy(proxyTargetClass=true)
@EnableFeignClients
@EnableHystrix
@EnableCaching
@EnableAsync
@EnableAscentSecurity
@Import(AscentDemoServiceConfig.class)
public class AscentDemoServiceApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AscentDemoServiceApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AscentDemoServiceApplication.class, args);
    } 
    
    @Bean
    AlwaysSampler alwaysSampler() {
        return new AlwaysSampler();
    }

}

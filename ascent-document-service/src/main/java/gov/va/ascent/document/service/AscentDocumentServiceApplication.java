package gov.va.ascent.document.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableDiscoveryClient 
@EnableAspectJAutoProxy(proxyTargetClass=true)
@EnableFeignClients
@EnableHystrix
public class AscentDocumentServiceApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AscentDocumentServiceApplication.class);
    }	
    public static void main(String[] args) throws Exception {
        SpringApplication.run(AscentDocumentServiceApplication.class, args);
    } 
    
    @Bean
    AlwaysSampler alwaysSampler() {
        return new AlwaysSampler();
    }	
}

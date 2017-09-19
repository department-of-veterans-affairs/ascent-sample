package gov.va.ascent.document.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebMvcProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.DispatcherServlet;

/* An <tt>Ascent Document Demo Service Application</tt> enabled for Spring Boot Application, 
* Spring Cloud Netflix Feign Clients, Hystrix circuit breakers, Swagger and 
* AspectJ's @Aspect annotation.
*
*/
@SpringBootApplication
@EnableDiscoveryClient //needed to reach out to spring cloud config, eureka
@EnableAspectJAutoProxy(proxyTargetClass=true)
@EnableFeignClients
@EnableHystrix
@EnableCaching
@EnableAsync
public class AscentDocumentServiceApplication extends SpringBootServletInitializer {

    @Autowired
    private WebMvcProperties webMvcProperties;
  
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
    
    @Bean
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setDispatchOptionsRequest(
                this.webMvcProperties.isDispatchOptionsRequest());
        dispatcherServlet.setDispatchTraceRequest(
                this.webMvcProperties.isDispatchTraceRequest());
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(
                this.webMvcProperties.isThrowExceptionIfNoHandlerFound());
        dispatcherServlet.setThreadContextInheritable(true);
        return dispatcherServlet;
    }
}

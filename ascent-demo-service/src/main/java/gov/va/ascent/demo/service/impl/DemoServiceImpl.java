package gov.va.ascent.demo.service.impl;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;

import gov.va.ascent.demo.service.AscentDemoServiceProperties;
import gov.va.ascent.demo.service.api.DemoService;
import gov.va.ascent.demo.service.api.v1.transfer.Demo;
import gov.va.ascent.demo.service.api.v1.transfer.DemoServiceResponse;
import gov.va.ascent.demo.service.utils.HystrixCommandConstants;

@Component
@Qualifier("IMPL")
@RefreshScope
@DefaultProperties(groupKey = HystrixCommandConstants.ASCENT_DEMO_SERVICE_GROUP_KEY)
public class DemoServiceImpl implements DemoService {
	final static Logger LOGGER = LoggerFactory.getLogger(DemoServiceImpl.class);
	
	@Autowired
	private AscentDemoServiceProperties properties;
	
	@Value("${ascent-demo-service.sampleProperty}") 
	private String sampleProperty;


	@Override
	@HystrixCommand(fallbackMethod = "getFallbackDemoResponse", commandKey = "DemoServiceReadCommand")
	public DemoServiceResponse read(String name){
		if ("error".equals(name)) {
			throw new RuntimeException("thrown on purpose!");
		}
		DemoServiceResponse response = new DemoServiceResponse();
		Demo demo = new Demo();
		demo.setName(name);
		demo.setDescription("description for demo with name: " + name + ".  " +
				"FYI Sample property is as followed from 2 different sources " + 
				"[AscentDemoServiceConfig] and [Autowired]: [" + 
				properties.getSampleProperty() + "][" + sampleProperty + "]");
		response.setDemo(demo);
		return response;
	}
	
    @Override
    @HystrixCommand(fallbackMethod = "getFallbackDemoResponse", commandKey = "DemoServiceAsyncReadCommand")
    public Future<DemoServiceResponse> readAsync(final String name) {
    	if ("error".equals(name)) {
			throw new RuntimeException("thrown on purpose!");
		}
        return new AsyncResult<DemoServiceResponse>() {
            @Override
            public DemoServiceResponse invoke() {
            	DemoServiceResponse response = new DemoServiceResponse();
        		Demo demo = new Demo();
        		demo.setName(name);
        		demo.setDescription("ASYNC CALL: description for demo with name: " + name + ".  " +
        				"FYI Sample property is as followed from 2 different sources " + 
        				"[AscentDemoServiceConfig] and [Autowired]: [" + 
        				properties.getSampleProperty() + "][" + sampleProperty + "]");
        		response.setDemo(demo);
        		try {
        			if ("sleep".equals(name)) {
        				Thread.sleep(15000);
        			}
				} catch (InterruptedException e) {
					throw new RuntimeException("thrown on purpose!");
				}
        		return response;
            }
        };
    }

	public DemoServiceResponse getFallbackDemoResponse(String name) {
		DemoServiceResponse response = new DemoServiceResponse();
		Demo demo = new Demo();
		demo.setName(name);
		demo.setDescription("Fallback Response to the client");
		response.setDemo(demo);
		return response;
    }
}

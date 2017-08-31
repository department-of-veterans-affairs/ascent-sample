package gov.va.ascent.demo.service.config;

import gov.va.ascent.demo.service.rest.client.discovery.DemoUsageDiscoveryClient;
import gov.va.ascent.security.jwt.JwtTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Configuration
public class DemoServiceRestClientTestsConfig {
	
	@Bean
    DemoUsageDiscoveryClient demoUsageDiscoveryClient() {
        return new DemoUsageDiscoveryClient();
    }
	
}

@Component
class TokenClientHttpRequestInterceptor implements ClientHttpRequestInterceptor{

    private final static Logger LOGGER = LoggerFactory.getLogger(TokenClientHttpRequestInterceptor.class);

    @Autowired
    private JwtTokenService tokenService;

    @Override
    public final ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
                                              final ClientHttpRequestExecution execution)
            throws IOException {

        Map<String, String> tokenMap =  tokenService.getTokenFromRequest();
        for(String token: tokenMap.keySet()){
            LOGGER.info("Adding Token Header {} {}", token, tokenMap.get(token));
            request.getHeaders().add(token, tokenMap.get(token));
        }
        return execution.execute(request, body);
    }
}
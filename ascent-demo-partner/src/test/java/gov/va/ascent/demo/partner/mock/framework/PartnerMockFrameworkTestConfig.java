package gov.va.ascent.demo.partner.mock.framework;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Fake Spring configuration used to test the partner mock framework classes
 * 
 * @author jshrader
 */
@Configuration
@Import({ PartnerMockFrameworkTestPropertiesConfig.class, PartnerMockFrameworkConfig.class })
public class PartnerMockFrameworkTestConfig {

}

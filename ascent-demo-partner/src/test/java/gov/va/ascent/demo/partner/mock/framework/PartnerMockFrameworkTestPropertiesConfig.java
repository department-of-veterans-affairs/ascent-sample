package gov.va.ascent.demo.partner.mock.framework;

import gov.va.ascent.framework.config.BasePropertiesConfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Fake Spring configuration of the properties files (in real apps this project won't ship property files, apps provide those)
 * 
 * @author Jon Shrader
 */
@Configuration
public class PartnerMockFrameworkTestPropertiesConfig extends BasePropertiesConfig {

	/** The Constant APP_NAME. */
	public static final String APP_NAME = "wss-partner-mock-framework-test";

	/** The Constant DEFAULT_PROPERTIES. */
	private static final String DEFAULT_PROPERTIES = "classpath:/config/" + APP_NAME + ".properties";

	/**
	 * protected utility class constructor
	 */
	protected PartnerMockFrameworkTestPropertiesConfig() {
	}

	/**
	 * The local environment configuration.
	 */
	@Configuration
	@PropertySource(DEFAULT_PROPERTIES)
	static class DefaultEnvironment extends BasePropertiesEnvironment {
	}

}

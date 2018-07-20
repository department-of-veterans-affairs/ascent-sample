package gov.va.ascent.demo.partner.mock.framework;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import gov.va.ascent.framework.config.AscentCommonSpringProfiles;

/**
 * Spring configuration for the project.
 * 
 * @author Jon Shrader
 */
@Configuration
@Profile({AscentCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS, PartnerMockFrameworkConfig.PROFILE_PARTNER_MOCK_FRAMEWORK})
@ComponentScan(basePackages = "gov.va.ascent.demo.partner.mock.framework", excludeFilters = @Filter(Configuration.class))
public class PartnerMockFrameworkConfig {

	/** Activates the partner mock framework */
	public static final String PROFILE_PARTNER_MOCK_FRAMEWORK = "partner_mock_framework";
	

}

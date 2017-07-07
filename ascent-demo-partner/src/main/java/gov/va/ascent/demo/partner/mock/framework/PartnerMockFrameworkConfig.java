package gov.va.ascent.demo.partner.mock.framework;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import gov.va.ascent.framework.config.AscentCommonSpringProfiles;
import gov.va.ascent.framework.persist.Db4oDatabase;

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
	
	/**
	 * sets up db4o database bean.
	 * 
	 * @param db4oFile the db4o file
	 * @param clientServerMode the client server mode
	 * @param port the port
	 * @return db4o database
	 */
	//jshrader - cannot make this Spring @Bean final
	//CHECKSTYLE:OFF
	@Bean
	Db4oDatabase partnerMockDb(
			//CHECKSTYLE:ON
			@Value("${wss-partner-mock-framework.db4oFile:}") final String db4oFile,
			@Value("${wss-partner-mock-framework.db4oFile.clientServerMode:true}") final Boolean clientServerMode,
			@Value("${wss-partner-mock-framework.db4oFile.port:9456}") final int port) {
		final Db4oDatabase db4oDatabase = new Db4oDatabase();
		if(db4oFile == null || StringUtils.isEmpty(db4oFile)){
			db4oDatabase.setDb4oFile("partnerMockDb_" + port + ".db4o");	
		} else {
			db4oDatabase.setDb4oFile(db4oFile);			
		}
		db4oDatabase.setClientServerMode(clientServerMode);
		db4oDatabase.setPort(port);
		return db4oDatabase;
	}

}

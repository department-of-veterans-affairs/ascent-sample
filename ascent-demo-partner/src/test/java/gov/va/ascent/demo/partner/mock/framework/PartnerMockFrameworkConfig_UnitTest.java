package gov.va.ascent.demo.partner.mock.framework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;


/**
 * The primary purpose of this test is to load the spring context files for the project
 * and validate they can load properly.  
 * 
 * @author jshrader
 */
public class PartnerMockFrameworkConfig_UnitTest extends AbstractPartnerMockFrameworkSpringIntegratedTest {

	@Autowired
    private ApplicationContext applicationContext;
	
}

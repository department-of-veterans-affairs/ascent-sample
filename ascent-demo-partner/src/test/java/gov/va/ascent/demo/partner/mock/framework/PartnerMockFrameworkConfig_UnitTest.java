package gov.va.ascent.demo.partner.mock.framework;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.va.ascent.framework.persist.Db4oDatabase;

import org.junit.Test;
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
	
	@Autowired 
	private Db4oDatabase partnerMockDb;
	
	@Test
	public void testContextLoaded() {
		assertNotNull(applicationContext);
		assertNotNull(partnerMockDb);
		assertTrue(applicationContext.containsBean("partnerMockDb"));	
	}
	
}

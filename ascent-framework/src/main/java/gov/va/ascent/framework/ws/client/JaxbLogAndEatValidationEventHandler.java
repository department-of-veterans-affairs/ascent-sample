package gov.va.ascent.framework.ws.client;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This custom ValidationEventHandler will not fail validations, rather will log and eat the validation
 * error.  This is due to the fact that numerous service operations may not be of the quality we desire
 * and return invalid responses.  We want to log the fact we encountered the response, however we need
 * to do our best to try to overcome the error and proceed with normal runtime operation of the app.
 * 
 * @author jshrader
 */
public class JaxbLogAndEatValidationEventHandler implements ValidationEventHandler  {

	/** logger for this class. */
	private static final Logger LOGGER = LoggerFactory.getLogger(JaxbLogAndEatValidationEventHandler.class);
	
	/** The Constant LOG_DESCRIPTION. */
	private static final String LOG_DESCRIPTION = "SCHEMA VALIDATION ERROR: ";
	
	/** Boolean flag to indicate if we should log the JAXB error as an error nor debug.
	 * In the test environment we get so many errors we don't want to polute logs, however in
	 * prod data is expected to be cleaner, logs less polluted and we may want these logged. */
	private final boolean logSchemaValidationFailureAsError;
	
	/**
	 * Instantiates a new jaxb log and eat validation event handler.
	 *
	 * @param logSchemaValidationFailureAsError the log schema validation failure as error
	 */
	public JaxbLogAndEatValidationEventHandler(final boolean logSchemaValidationFailureAsError) {
		this.logSchemaValidationFailureAsError = logSchemaValidationFailureAsError;
	}
	
	/* (non-Javadoc)
	 * @see javax.xml.bind.ValidationEventHandler#handleEvent(javax.xml.bind.ValidationEvent)
	 */
	@Override
	public final boolean handleEvent(final ValidationEvent event) {
		
		if (logSchemaValidationFailureAsError) {
			LOGGER.error(LOG_DESCRIPTION + event);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(LOG_DESCRIPTION + event);
		}
		
		return true;
	}

}

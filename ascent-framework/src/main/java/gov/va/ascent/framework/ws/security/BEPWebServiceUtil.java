package gov.va.ascent.framework.ws.security;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import gov.va.ascent.framework.security.SecurityUtils;
import gov.va.ascent.framework.util.Defense;
import gov.va.ascent.framework.util.HashGenerator;

/**
 * The Class BEPWebServiceUtil provides a common method of handling elements 
 * of web service calls made to VA BEP layer.
 */
public final class BEPWebServiceUtil {
	
	/** The Constant EXTERNALUID_LENGTH. */
	public static final int EXTERNALUID_LENGTH = 39;
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(BEPWebServiceUtil.class);
	
	/**
	 * Private constructor to prevent instantiation.
	 */
	private BEPWebServiceUtil() {
	}

	/**
	 * Gets the External UID based on the below BEP specification. <br/>
	 * User’s identifier (user name, user id, etc…) corresponding to the caller’s identity in the client application. 
	 * Do not use this element for internal application. 39 characters is the limit. <br/>
	 * The function will get the User ID (name) as recorded in the EVSS User table. 
	 * If the ID is null, then the supplied default is used.
	 * If the ID meets the specification, then it is returned as-is.
	 * Else, the ID is converted to an MD5 digest.
	 *
	 * @param defaultVal the default val
	 * @return the external uid
	 */
	public static String getExternalUID(final String defaultVal) {
		String computedVal = getComputedValue(SecurityUtils.getUserId(), defaultVal);
		
		if (computedVal.length() > EXTERNALUID_LENGTH ) {
			try {
				computedVal=HashGenerator.getMd5ForString(computedVal);
			} catch(NoSuchAlgorithmException e) {
				LOGGER.error(e.getMessage(), e);
				computedVal=null;
			}
		}
		
		Defense.notNull(computedVal);
		Assert.isTrue(computedVal.length()<=EXTERNALUID_LENGTH);
		return computedVal;
	}

	/**
	 * Gets the External Key based on the below BEP specification. <br/>
	 * User’s system identifier associated with the caller's identity (if the ExternalUid represents 
	 * a unique identifier of the user’s identity, then this value is equal to the ExternalUid, 
	 * otherwise represents a unique id – such a PK). Do not user this element for internal applications. 
	 * 39 characters is the limit. <br/>
	 * 
	 * This function simply proxies to getExternalUid for now.
	 *
	 * @param defaultVal the default val
	 * @return the external key
	 */
	public static String getExternalKey(final String defaultVal) {
		return getExternalUID(defaultVal);
	}

	/**
	 * Gets the client machine value. BEP specification - User’s workstation IP address.
	 *
	 * @param defaultVal the default val
	 * @return the client machine
	 */
	public static String getClientMachine(final String defaultVal) {
		String computedVal =null;
		try {
			computedVal = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			LOGGER.error(e.getMessage(), e);
			// handled further down
		}
		return getComputedValue(computedVal, defaultVal);
	}
	
	/**
	 * Helper method that adds null checking to cumputedVal and defaultVal.
	 *
	 * @param computedVal the computed val
	 * @param defaultVal the default val
	 * @return the computed value
	 */
	private static String getComputedValue(String computedVal, String defaultVal) {
		String returnVal = computedVal;
		if (returnVal==null) {
			returnVal=defaultVal;
		}
		Defense.notNull(returnVal);
		return returnVal;
	}
	
}

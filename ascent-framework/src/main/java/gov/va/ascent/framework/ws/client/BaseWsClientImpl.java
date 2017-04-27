package gov.va.ascent.framework.ws.client;


/**
 * The base class for Web Service client implementations, containing utility
 * operations, etc. that are likely reusable across such implementations.
 * 
 * @author jshrader
 */
public class BaseWsClientImpl {

	/** The response from webservice call null. */
	protected static final String RESPONSE_FROM_WEBSERVICE_CALL_NULL = "Response from web service call null.";

	/** The request for webservice call null. */
	protected static final String REQUEST_FOR_WEBSERVICE_CALL_NULL = "Request for web service call null.";
	
	/**
	 * This is not an abstract class, however it is a base class that is not to be instantiated. 
	 * In this case, it's probably better to use a private or a protected constructor in order to 
	 * prevent instantiation than make the class misleadingly abstract.
	 */
	protected BaseWsClientImpl(){}

}
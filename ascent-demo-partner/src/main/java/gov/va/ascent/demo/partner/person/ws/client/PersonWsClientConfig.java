package gov.va.ascent.demo.partner.person.ws.client;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;

import gov.va.ascent.framework.exception.InterceptingExceptionTranslator;
import gov.va.ascent.framework.log.PerformanceLogMethodInterceptor;
import gov.va.ascent.framework.ws.client.BaseWsClientConfig;
import gov.va.ascent.framework.ws.client.WsClientSimulatorMarshallingInterceptor;

/**
 * This class represents the Spring configuration for the Person Web Service Client.
 */
@Configuration
@ComponentScan(basePackages = { "gov.va.ascent.demo.partner.person.ws.client" },
		excludeFilters = @Filter(Configuration.class))
public class PersonWsClientConfig extends BaseWsClientConfig {

	/**
	 * The Constant TRANSFER_PACKAGE.
	 */
	private static final String TRANSFER_PACKAGE = "gov.va.ascent.demo.partner.person.ws.transfer";

	/**
	 * The username.
	 */
	@Value("${wss-partner-person.ws.client.username}")
	private String username;

	/**
	 * The password.
	 */
	@Value("${wss-partner-person.ws.client.password}")
	private String password;

	/**
	 * The va application name.
	 */
	@Value("${wss-partner-person.ws.client.vaApplicationName}")
	private String vaApplicationName;

	/**
	 * VA STN_ID value
	 */
	@Value("${wss-partner-person.ws.client.stationId}")
	private String stationId;

	/**
	 * decides if jaxb validation logs errors.
	 */
	@Value("${wss-common-services.ws.log.jaxb.validation:false}")
	private boolean logValidation;

	/**
	 * WS Client object marshaller
	 *
	 * @return object marshaller
	 */
	// Ignoring DesignForExtension check, we cannot make this spring bean method private or final
	// CHECKSTYLE:OFF
	@Bean
	@Qualifier("personWsClient")
	Jaxb2Marshaller personMarshaller() {
		final Resource[] schemas = new Resource[] { new ClassPathResource("xsd/PersonService/PersonWebService.xsd") };
		return getMarshaller(TRANSFER_PACKAGE, schemas, logValidation);
	}

	/**
	 * Axiom based WebServiceTemplate for the Person Web Service Client.
	 *
	 * @param endpoint the endpoint
	 * @param readTimeout the read timeout
	 * @param connectionTimeout the connection timeout
	 * @return the web service template
	 * @throws KeyManagementException the key management exception
	 * @throws UnrecoverableKeyException the unrecoverable key exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws KeyStoreException the key store exception
	 * @throws CertificateException the certificate exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	// Ignoring DesignForExtension check, we cannot make this spring bean method private or final
	// CHECKSTYLE:OFF
	@Bean
	@Qualifier("personWsClient.axiom")
	WebServiceTemplate personWsClientAxiomTemplate(
			// CHECKSTYLE:ON
			@Value("${wss-partner-person.ws.client.endpoint}") final String endpoint,
			@Value("${wss-partner-person.ws.client.readTimeout:60000}") final int readTimeout,
			@Value("${wss-partner-person.ws.client.connectionTimeout:60000}") final int connectionTimeout) {

		return createDefaultWebServiceTemplate(endpoint, readTimeout, connectionTimeout, personMarshaller(), personMarshaller(),
				new ClientInterceptor[] { personSecurityInterceptor() });
	}

	/**
	 * Security interceptor to apply WSS security to Person WS calls.
	 *
	 * @return security interceptor
	 */
	// jluck - ignoring DesignForExtension check, we cannot make this spring
	// bean method private or final
	// CHECKSTYLE:OFF
	@Bean
	Wss4jSecurityInterceptor personSecurityInterceptor() {
		// CHECKSTYLE:ON
		return getVAServiceWss4jSecurityInterceptor(username, password, vaApplicationName, stationId);
	}

	/**
	 * PerformanceLogMethodInterceptor for the Person Web Service Client
	 * <p>
	 * Handles performance related logging of the web service client response times.
	 *
	 * @param methodWarningThreshhold the method warning threshold
	 * @return the performance log method interceptor
	 */
	// Ignoring DesignForExtension check, we cannot make this spring bean method private or final
	// CHECKSTYLE:OFF
	@Bean
	PerformanceLogMethodInterceptor personWsClientPerformanceLogMethodInterceptor(
			@Value("${wss-partner-person.ws.client.methodWarningThreshhold:2500}") final Integer methodWarningThreshhold) {
		// CHECKSTYLE:ON
		return getPerformanceLogMethodInterceptor(methodWarningThreshhold);
	}

	/**
	 * InterceptingExceptionTranslator for the Person Web Service Client
	 * <p>
	 * Handles runtime exceptions raised by the web service client through runtime operation and communication with the remote service.
	 *
	 * @return the intercepting exception translator
	 * @throws ClassNotFoundException the class not found exception
	 */
	// Ignoring DesignForExtension check, we cannot make this spring bean method private or final
	// CHECKSTYLE:OFF
	@SuppressWarnings("unchecked")
	@Bean
	InterceptingExceptionTranslator personWsClientExceptionInterceptor() throws ClassNotFoundException {
		// CHECKSTYLE:ON

		// CHECKSTYLE:ON
		final InterceptingExceptionTranslator interceptingExceptionTranslator = new InterceptingExceptionTranslator();

		// set the default type of exception that should be returned when this
		// interceptor runs
		interceptingExceptionTranslator.setDefaultExceptionType(
				(Class<? extends RuntimeException>) Class
						.forName("gov.va.ascent.demo.partner.person.ws.client.PersonWsClientException"));

		// define packages that contain "our exceptions" that we want to
		// propagate through
		// without again logging and/or wrapping
		final Set<String> exclusionSet = new HashSet<>();
		exclusionSet.add(PACKAGE_ASCENT_FRAMEWORK_EXCEPTION);
		interceptingExceptionTranslator.setExclusionSet(exclusionSet);

		return interceptingExceptionTranslator;

	}

	/**
	 * A standard bean proxy to apply interceptors to the Address web service client.
	 *
	 * @return the bean name auto proxy creator
	 */
	// Ignoring DesignForExtension check, we cannot make this spring bean method private or final
	// CHECKSTYLE:OFF
	@Bean
	BeanNameAutoProxyCreator personWsClientBeanProxy() {
		// CHECKSTYLE:ON
		return getBeanNameAutoProxyCreator(new String[] { PersonWsClientImpl.BEAN_NAME, PersonWsClientSimulator.BEAN_NAME },
				new String[] { "personWsClientExceptionInterceptor", "personWsClientPerformanceLogMethodInterceptor" });
	}

	/**
	 * Ws client simulator marshalling interceptor, so that requests and responses to the simulator are passed through the marshaller
	 * to ensure we don't have any Java-to-XML conversion surprises if we leverage simulators heavily in development and then start
	 * using real web services later on.
	 *
	 * @return the ws client simulator marshalling interceptor
	 */
	// Ignoring DesignForExtension check, we cannot make this spring bean method private or final
	// CHECKSTYLE:OFF
	@Bean
	WsClientSimulatorMarshallingInterceptor personWsClientSimulatorMarshallingInterceptor() {
		// CHECKSTYLE:ON
		final Map<String, Jaxb2Marshaller> marshallerForPackageMap = new HashMap<>();
		marshallerForPackageMap.put(TRANSFER_PACKAGE, personMarshaller());
		return new WsClientSimulatorMarshallingInterceptor(marshallerForPackageMap);
	}

	/**
	 * A standard bean proxy to apply interceptors to the web service client simulations that we don't need/want to apply to real web
	 * service client impls.
	 *
	 * @return the bean name auto proxy creator
	 */
	// Ignoring DesignForExtension check, we cannot make this spring bean method private or final
	// CHECKSTYLE:OFF
	@Bean
	BeanNameAutoProxyCreator personWsClientSimulatorProxy() {
		// CHECKSTYLE:ON
		return getBeanNameAutoProxyCreator(new String[] { PersonWsClientSimulator.BEAN_NAME },
				new String[] { "personWsClientSimulatorMarshallingInterceptor" });
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}

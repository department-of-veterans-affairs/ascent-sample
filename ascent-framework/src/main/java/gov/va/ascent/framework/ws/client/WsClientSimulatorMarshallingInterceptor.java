package gov.va.ascent.framework.ws.client;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.stream.StreamResult;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import gov.va.ascent.framework.util.Defense;

/**
 * This interceptor is intended to wrap all of the web service simulators. Apply
 * a marshaller onto the input and output objects to ensure we are able to
 * marshall requests and responses to XML. This is to helping prevent issues
 * transitioning to/from web services to simulators.
 * 
 * @author jshrader
 */
public class WsClientSimulatorMarshallingInterceptor implements
		MethodInterceptor {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WsClientSimulatorMarshallingInterceptor.class);

	/** Error message constant */
	private static final String XML_ROOT_ERROR = 
			" is not an @XmlRootElement, and no ObjectFactory method was found to construct it.";

	/** The map which maps packages to their respective marshallers */
	private final Map<String, Jaxb2Marshaller> marshallerForPackageMap;

	/** The map which matches packages to their respective ObjectFactories */
	private final Map<String, Object> objectFactoryForPackageMap;

	/**
	 * Instantiates a new ws client simulator marshalling interceptor.
	 * 
	 * @param marshallerForPackageMap
	 *            the marshaller for package map
	 */
	public WsClientSimulatorMarshallingInterceptor(
			final Map<String, Jaxb2Marshaller> marshallerForPackageMap) {
		super();
		this.marshallerForPackageMap = marshallerForPackageMap;
		this.objectFactoryForPackageMap = new HashMap<String, Object>();
	}

	/**
	 * Instantiates a new ws client simulator marshalling interceptor.
	 * 
	 * @param marshallerForPackageMap
	 *            the marshaller for package map
	 * @param objectFactoryForPackageMap
	 *            the objectFactory for package map
	 */
	public WsClientSimulatorMarshallingInterceptor(
			final Map<String, Jaxb2Marshaller> marshallerForPackageMap,
			final Map<String, Object> objectFactoryForPackageMap) {
		super();
		this.marshallerForPackageMap = marshallerForPackageMap;
		this.objectFactoryForPackageMap = objectFactoryForPackageMap;
	}

	/**
	 * Post construct.
	 */
	@PostConstruct
	public final void postConstruct() {
		Defense.notNull(
				marshallerForPackageMap,
				"marshallerForPackageMap cannot be null in order for WsClientSimulatorMarshallingInterceptor to work!");
	}

	/**
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	// JSHRADER throws throwable part of the interface, unavoidable
	// CHECKSTYLE:OFF
	public final Object invoke(final MethodInvocation methodInvocation)
			throws Throwable {
		// CHECKSTYLE:ON

		final Object request = methodInvocation.getArguments()[0];

		// find the appropriate marshaller for the Request and marshall it to
		// XML
		if (request != null) {
			final Jaxb2Marshaller marshaller = getJaxbMarshaller(request);

			if (marshaller != null) {
				final String marshalled = marshall(marshaller, request);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("marshalled request xml: " + marshalled);
				}
			} else {
				LOGGER.warn("no marshaller found for request, not marshalling. "
						+ request);
			}

		} else {
			LOGGER.warn("request null, not attempting to marshall.");
		}

		final Object response = methodInvocation.proceed();

		// find the appropriate marshaller for the Request and marshall it to
		// XML
		if (response != null) {
			final Jaxb2Marshaller marshaller = getJaxbMarshaller(response);

			if (marshaller != null) {
				final String marshalled = marshall(marshaller, response);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("marshalled response xml: " + marshalled);
				}
			} else {
				LOGGER.warn("no marshaller found for response, not marshalling. "
						+ response);
			}

		} else {
			LOGGER.warn("response null, not attempting to marshall.");
		}

		return response;
	}

	/**
	 * Gets the jaxb marshaller for the request or response object.
	 * 
	 * @param obj
	 *            the obj
	 * @return the jaxb marshaller
	 */
	@SuppressWarnings("rawtypes")
	private Jaxb2Marshaller getJaxbMarshaller(final Object obj) {
		// find the correct marshaller based on package
		Jaxb2Marshaller marshaller = null;
		if (obj instanceof JAXBElement) {
			marshaller = marshallerForPackageMap.get(((JAXBElement) obj)
					.getValue().getClass().getPackage().getName());
		} else {
			marshaller = marshallerForPackageMap.get(obj.getClass()
					.getPackage().getName());
		}
		return marshaller;
	}

	/**
	 * Get the ObjectFactory class associated with the given request/response
	 * object.
	 * 
	 * @param obj
	 *            request/response object
	 * @return ObjectFactory instance
	 */
	@SuppressWarnings("rawtypes")
	private Object getObjectFactory(final Object obj) {
		// find the correct object factory based on package
		Object objectFactory = null;
		if (obj instanceof JAXBElement) {
			objectFactory = objectFactoryForPackageMap.get(((JAXBElement) obj)
					.getValue().getClass().getPackage().getName());
		} else {
			objectFactory = objectFactoryForPackageMap.get(obj.getClass()
					.getPackage().getName());
		}
		return objectFactory;
	}

	/**
	 * Marshall the objects to XML using the jaxb binder The purpose of this is
	 * to make sure that we don't encounter major surprises when we stop using
	 * simulators and start using XML based web services. All of our requests
	 * and faked responses should adhere to schemas, this will ensure adherence.
	 * 
	 * If marshalling of the given object fails, the method will attempt to use
	 * an associated ObjectFactory class to construct the bound object using a
	 * method with the signature create<obj.getClass().getName()>. This there is
	 * an associated ObjectFactory method, the result of the method will be
	 * attempt to be marshalled instead.
	 * 
	 * @param marshaller
	 *            the marshaller to use for the object
	 * @param obj
	 *            the object to marshal
	 * @return the XML
	 */
	private String marshall(final Jaxb2Marshaller marshaller, final Object obj) {
		final StreamResult result = new StreamResult(
				new ByteArrayOutputStream());
		Object objectToMarshal = obj;
		if (!obj.getClass().isAnnotationPresent(XmlRootElement.class)) {
			// If not a XmlRootElement, try wrapping it in a JAXBElement
			// using an associated ObjectFactory class
			final Object objectFactory = getObjectFactory(obj);
			if (objectFactory != null) {
				final String methodName = "create"
						+ obj.getClass().getSimpleName();

				try {
					final Method createMethod = objectFactory.getClass()
							.getMethod(methodName, obj.getClass());
					objectToMarshal = createMethod.invoke(objectFactory, obj);
				} catch (NoSuchMethodException methodEx) {
					LOGGER.warn(obj.getClass().getName() + XML_ROOT_ERROR);
				} catch (IllegalArgumentException e) {
					LOGGER.warn(obj.getClass().getName() + XML_ROOT_ERROR);
				} catch (IllegalAccessException e) {
					LOGGER.warn(obj.getClass().getName() + XML_ROOT_ERROR);
				} catch (InvocationTargetException e) {
					LOGGER.warn(obj.getClass().getName() + XML_ROOT_ERROR);
				}
			}
		}

		marshaller.marshal(objectToMarshal, result);
		return result.getOutputStream().toString();
	}

}

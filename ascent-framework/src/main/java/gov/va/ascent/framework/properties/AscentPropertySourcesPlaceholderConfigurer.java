package gov.va.ascent.framework.properties;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.core.env.Environment;

/**
 * Used to hook in our property resolvers so we can collect data on the app.
 * 
 * @author jely
 * 
 */
public class AscentPropertySourcesPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer {

	/** The configurable environment. */
	private ConfigurableEnvironment configurableEnvironment;

	/** The property source resolver wss. */
	private final AscentPropertySourcesPropertyResolver propertySourceResolverWss = new AscentPropertySourcesPropertyResolver();

	/**
	 * Gets the active profiles.
	 * 
	 * @return the active profiles
	 */
	public final String[] getActiveProfiles() {
		return configurableEnvironment.getActiveProfiles();
	}

	/**
	 * Gets the default profiles.
	 * 
	 * @return the default profiles
	 */
	public final String[] getDefaultProfiles() {
		return configurableEnvironment.getDefaultProfiles();
	}

	/**
	 * Gets the property info.
	 * 
	 * @return the property info
	 */
	public final List<PropertyFileHolder> getPropertyInfo() {
		return propertySourceResolverWss.getPropertyFileHolders();
	}

	/**
	 * Gets the property source resolver wss.
	 * 
	 * @return the property source resolver wss
	 */
	public final AscentPropertySourcesPropertyResolver getPropertySourceResolverWss() {
		return propertySourceResolverWss;
	}

	/**
	 * Gets the system properties.
	 * 
	 * @return the system properties
	 */
	public final Map<String, Object> getSystemProperties() {
		return configurableEnvironment.getSystemProperties();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.support.PropertySourcesPlaceholderConfigurer#processProperties(org.springframework.beans.factory
	 * .config.ConfigurableListableBeanFactory, org.springframework.core.env.ConfigurablePropertyResolver)
	 */
	@Override
	protected final void processProperties(final ConfigurableListableBeanFactory beanFactoryToProcess,
			final ConfigurablePropertyResolver propertyResolver) {
		propertySourceResolverWss.setPropertySourcesPropertyResolver(propertyResolver);
		propertySourceResolverWss.setPropertySources(configurableEnvironment.getPropertySources());
		super.processProperties(beanFactoryToProcess, propertySourceResolverWss);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.support.PropertySourcesPlaceholderConfigurer#setEnvironment(org.springframework.core.env.Environment
	 * )
	 */
	@Override
	public final void setEnvironment(final Environment environment) {
		configurableEnvironment = (ConfigurableEnvironment) environment;
		super.setEnvironment(environment);
	}

}

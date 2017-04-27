package gov.va.ascent.framework.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.util.StringUtils;

/**
 * Class used to collect info on what properties are being used in the application.
 * 
 * @author 31964
 * 
 */
public class AscentPropertySourcesPropertyResolver extends PropertySourcesPropertyResolver {

	/** The Constant DEFAULT_PROPERTIES. */
	private static final String DEFAULT_PROPERTIES = "Default Properties Not loaded from file but default values in app";

	/** The property sources property resolver. */
	private PropertyResolver propertySourcesPropertyResolver;

	/** The property sources. */
	@Autowired
	private PropertySources propertySources;

	/** The value separator. */
	private String valueSeparator = ":";

	/** The property file holders. */
	private final List<PropertyFileHolder> propertyFileHolders = new ArrayList<PropertyFileHolder>();

	/**
	 * Instantiates a new WSS property sources property resolver.
	 */
	public AscentPropertySourcesPropertyResolver() {
		super(null);
	}

	/**
	 * Adds the file name and value.
	 * 
	 * @param <T> the generic type
	 * @param key the key
	 * @param value the value
	 */
	private <T> void addFileNameAndValue(final String key, final T value) {
		for (final PropertySource<?> propertySource : propertySources) {
			if (propertySource.getProperty(key) != null) {
				// if it is in a property file
				addPropertyToList(key, value, propertySource.getName());
				return;
			}

		}
		if (value == null) {
			final String[] strings = StringUtils.split(key, valueSeparator);
			// if this is null spring removes values from the variable trying to find the default value and the key without the default
			// on it and it will try and come back with nothing there fore making this chunk wqere it rips off the default already done
			if (strings != null) {
				// if it is loaded as a default value
				addPropertyToList(strings[0], strings[1], DEFAULT_PROPERTIES);
			}
		} else {
			// if it is loaded as a default value
			addPropertyToList(key, value, DEFAULT_PROPERTIES);
		}

	}

	/**
	 * Adds the property to list.
	 * 
	 * @param <T> the generic type
	 * @param key the key
	 * @param value the value
	 * @param fileName the file name
	 */
	private <T> void addPropertyToList(final String key, final T value, final String fileName) {
		for (final PropertyFileHolder fileHolder : propertyFileHolders) {
			if (fileName.equals(fileHolder.getName())) {
				fileHolder.getPropertyInfo().put(key, value);
				return;
			}
		}
		final PropertyFileHolder fileHolder = new PropertyFileHolder();
		fileHolder.setName(fileName);
		fileHolder.getPropertyInfo().put(key, value);
		propertyFileHolders.add(fileHolder);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.core.env.PropertySourcesPropertyResolver#containsProperty(java.lang.String)
	 */
	@Override
	public final boolean containsProperty(final String key) {
		if (propertySourcesPropertyResolver != null) {
			return propertySourcesPropertyResolver.containsProperty(key);
		} else {
			return super.containsProperty(key);
		}
	}

	/**
	 * Gets the from file property.
	 * 
	 * @param key the key
	 * @return the from file property
	 */
	public final String getFromFileProperty(final String key) {
		String value = "";
		if (propertySourcesPropertyResolver != null) {
			value = propertySourcesPropertyResolver.getProperty(key);
		} else {
			value = super.getProperty(key);
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.core.env.PropertySourcesPropertyResolver#getProperty(java.lang.String)
	 */
	@Override
	public final String getProperty(final String key) {
		String value = "";
		if (propertySourcesPropertyResolver != null) {
			value = propertySourcesPropertyResolver.getProperty(key);
		} else {
			value = super.getProperty(key);
		}
		addFileNameAndValue(key, value);
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.core.env.PropertySourcesPropertyResolver#getProperty(java.lang.String, java.lang.Class)
	 */
	@Override
	public final <T> T getProperty(final String key, final Class<T> targetValueType) {
		T value;
		if (propertySourcesPropertyResolver != null) {
			value = propertySourcesPropertyResolver.getProperty(key, targetValueType);
		} else {
			value = super.getProperty(key, targetValueType);
		}
		addFileNameAndValue(key, value);
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.core.env.AbstractPropertyResolver#getProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public final String getProperty(final String key, final String defaultValue) {
		String value = "";
		if (propertySourcesPropertyResolver != null) {
			value = propertySourcesPropertyResolver.getProperty(key, defaultValue);
		} else {
			value = super.getProperty(key, defaultValue);
		}
		addFileNameAndValue(key, value);
		return value;
	}

	/**
	 * Gets the property file holders.
	 * 
	 * @return the property file holders
	 */
	public final List<PropertyFileHolder> getPropertyFileHolders() {
		return propertyFileHolders;
	}

	/**
	 * Sets the property sources.
	 * 
	 * @param propertySources the new property sources
	 */
	public final void setPropertySources(final PropertySources propertySources) {
		propertySourcesPropertyResolver = new PropertySourcesPropertyResolver(propertySources);
		this.propertySources = propertySources;
	}

	/**
	 * Sets the property sources property resolver.
	 * 
	 * @param propertySourcesPropertyResolver the new property sources property resolver
	 */
	public final void setPropertySourcesPropertyResolver(final PropertyResolver propertySourcesPropertyResolver) {
		this.propertySourcesPropertyResolver = propertySourcesPropertyResolver;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.core.env.AbstractPropertyResolver#setValueSeparator(java.lang.String)
	 */
	@Override
	public final void setValueSeparator(final String valueSeparator) {
		this.valueSeparator = valueSeparator;
		super.setValueSeparator(valueSeparator);
	}
}

package gov.va.ascent.framework.properties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author jely POJO for holding info on the properties loaded into the app from the propertyfile.
 * 
 */
public class PropertyFileHolder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4565846578999808201L;

	/** The name. */
	private String name;

	/** The property info. */
	private final Map<String, Object> propertyInfo = new HashMap<String, Object>();

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Gets the property info.
	 * 
	 * @return the property info
	 */
	public final Map<String, Object> getPropertyInfo() {
		return propertyInfo;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name the new name
	 */
	public final void setName(final String name) {
		this.name = name;
	}
}

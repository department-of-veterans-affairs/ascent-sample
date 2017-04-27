package gov.va.ascent.framework.transfer;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import gov.va.ascent.framework.validation.ModelValidator;
import gov.va.ascent.framework.validation.Validatable;
import gov.va.ascent.framework.validation.ViolationMessageParts;

public abstract class AbstractTransferObject implements Serializable, Validatable {

	private static final long serialVersionUID = 1640669713852272808L;
	
	/** The model validator. */
	protected static final transient ModelValidator MODEL_VALIDATOR = new ModelValidator();

	protected String[] getToStringEqualsHashExcludeFields() {
		return new String[] {};
	}

	/* (non-Javadoc)
	 * @see gov.va.ascent.framework.transfer.TransferObject#validate(java.util.Map)
	 */
	//jshrader This method is not final on purpose to allow subclasses to override
	@Override
	public Map<String, List<ViolationMessageParts>> validate(Map<String, List<ViolationMessageParts>> messages){
		if(messages == null){
			//jshrader allowing assignment of messages here in this framework class in case it is null
			//CHECKSTYLE:OFF
			messages = new LinkedHashMap<String, List<ViolationMessageParts>>();
			//CHECKSTYLE:ON
		}
		
		//validate this object
		MODEL_VALIDATOR.validateModel(this, messages, (Class[]) null);
		
		return messages;
	}
	
	/**
	 * Default implementation of toString. Leverages org.apache.commons.lang.builder.ToStringBuilder
	 * 
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(this);         
	    reflectionToStringBuilder.setExcludeFieldNames(getToStringEqualsHashExcludeFields());  
	    return reflectionToStringBuilder.toString();  
	}
	
	/**
	 * Default implementation of equals. Leverages org.apache.commons.lang.builder.EqualsBuilder
	 * 
	 * @param obj the obj
	 * @return true, if successful
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, getToStringEqualsHashExcludeFields());
	}

	/**
	 * Default implementation of hashCode. Leverages org.apache.commons.lang.builder.HashCodeBuilder
	 * 
	 * @return the int
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, getToStringEqualsHashExcludeFields());
	}
	
}

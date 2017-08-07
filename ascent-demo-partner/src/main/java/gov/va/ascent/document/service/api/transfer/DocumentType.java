package gov.va.ascent.document.service.api.transfer;

import gov.va.ascent.framework.transfer.AbstractTransferObject;

/**
 * The Class DocumentType.
 */
public final class DocumentType extends AbstractTransferObject {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2367374795268510224L;

	/** The type id. */
	private String typeId;

	/** The type description. */
	private String typeDescription;

	/** The category id. */
	private String categoryId;

	/** The category description. */
	private String categoryDescription;

	/** The type label. */
	private String typeLabel;

	/**
	 * Gets the type id.
	 *
	 * @return the type id
	 */
	public String getTypeId() {
		return typeId;
	}

	/**
	 * Sets the type id.
	 *
	 * @param typeId the new type id
	 */
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	/**
	 * Gets the type description.
	 *
	 * @return the type description
	 */
	public String getTypeDescription() {
		return typeDescription;
	}

	/**
	 * Sets the type description.
	 *
	 * @param typeDescription the new type description
	 */
	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}

	/**
	 * Gets the category id.
	 *
	 * @return the category id
	 */
	public String getCategoryId() {
		return categoryId;
	}

	/**
	 * Sets the category id.
	 *
	 * @param categoryId the new category id
	 */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * Gets the category description.
	 *
	 * @return the category description
	 */
	public String getCategoryDescription() {
		return categoryDescription;
	}

	/**
	 * Sets the category description.
	 *
	 * @param categoryDescription the new category description
	 */
	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	/**
	 * Gets the type label.
	 *
	 * @return the type label
	 */
	public String getTypeLabel() {
		return typeLabel;
	}

	/**
	 * Sets the type label.
	 *
	 * @param typeLabel the new type label
	 */
	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}

}


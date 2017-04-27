package gov.va.ascent.demo.partner.person.ws.client.transfer;

import gov.va.ascent.framework.transfer.AbstractTransferObject;

/**
 * 
 * This class represents the relevant subset of the data returned from the Person Web Service.
 * 
 */
public class PersonInfo extends AbstractTransferObject {

	/** The class version id. */
	private static final long serialVersionUID = 5791227842810442936L;
	
	/** The person's file number. */
	private String fileNumber;
	
	/** The person's first name. */
	private String firstName;
	
	/** the person's middle name. */
	private String middleName;
	
	/** the person's last name. */
	private String lastName;
	
	/** the person's participant id. */
	private Long participantId;
	
	/** the person's social security number. */
	private String ssn;

	/**
	 * Gets the file number.
	 *
	 * @return The person file number
	 */
	public final String getFileNumber() {
		return fileNumber;
	}

	/**
	 * Sets the file number.
	 *
	 * @param fileNumber The person file number
	 */
	public final void setFileNumber(final String fileNumber) {
		this.fileNumber = fileNumber;
	}

	/**
	 * Gets the first name.
	 *
	 * @return The person first name
	 */
	public final String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name.
	 *
	 * @param firstName The person first name
	 */
	public final void setFirstName(final String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Gets the last name.
	 *
	 * @return The person last name
	 */
	public final String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name.
	 *
	 * @param lastName The person last name
	 */
	public final void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the middle name.
	 *
	 * @return The person middle name
	 */
	public final String getMiddleName() {
		return middleName;
	}

	/**
	 * Sets the middle name.
	 *
	 * @param middleName The person middle name
	 */
	public final void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	/**
	 * Gets the participant id.
	 *
	 * @return The person participant Id
	 */
	public final Long getParticipantId() {
		return participantId;
	}

	/**
	 * Sets the participant id.
	 *
	 * @param participantId The person participant Id
	 */
	public final void setParticipantId(final Long participantId) {
		this.participantId = participantId;
	}

	/**
	 * Gets the ssn.
	 *
	 * @return The person SSN
	 */
	public final String getSsn() {
		return ssn;
	}

	/**
	 * Sets the ssn.
	 *
	 * @param ssn The person SSN
	 */
	public final void setSsn(final String ssn) {
		this.ssn = ssn;
	}

}


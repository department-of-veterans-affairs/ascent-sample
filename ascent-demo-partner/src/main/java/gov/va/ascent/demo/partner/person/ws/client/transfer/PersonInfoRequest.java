/*
 * 
 */
package gov.va.ascent.demo.partner.person.ws.client.transfer;

import gov.va.ascent.framework.service.ServiceRequest;

/**
 * A class to represent a request for PersonInfo from the Person Service.
 *
 */
public class PersonInfoRequest extends ServiceRequest {

	/** version id. */
	private static final long serialVersionUID = -1348027376496517555L;

	/** A String representing a social security number. */
	private String ssn;

	/** A String representing a social security number. */
	private Long participantID;

	/**
	 * Gets the participantId.
	 *
	 * @return the participantID
	 */
	public final Long getParticipantID() {
		return this.participantID;
	}

	/**
	 * Sets the participantId.
	 *
	 * @param participantID the participantID
	 */
	public final void setParticipantID(final Long participantID) {
		this.participantID = participantID;
	}

	/**
	 * Gets the ssn.
	 *
	 * @return the SSN
	 */
	public final String getSsn() {
		return this.ssn;
	}

	/**
	 * Sets the ssn.
	 *
	 * @param ssn the SSN
	 */
	public final void setSsn(final String ssn) {
		this.ssn = ssn;
	}
}

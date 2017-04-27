package gov.va.ascent.demo.partner.person.ws.client.transfer;

import gov.va.ascent.framework.service.ServiceResponse;

/**
 * A class to represent the data contained in the response
 * from the Person Service.
 *
 */
public class PersonInfoResponse extends ServiceResponse {

	/** Id for serialization. */
	private static final long serialVersionUID = 7327802119014249445L;
	
	/** A PersonInfo instance. */
	private PersonInfo personInfo;

	/**
	 * Gets the person info.
	 *
	 * @return A PersonInfo instance
	 */
	public final PersonInfo getPersonInfo() {
		return personInfo;
	}

	/**
	 * Sets the person info.
	 *
	 * @param personInfo A PersonInfo instance
	 */
	public final void setPersonInfo(final PersonInfo personInfo) {
		this.personInfo = personInfo;
	}
}

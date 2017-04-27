package gov.va.ascent.demo.service.api;

import gov.va.ascent.demo.partner.person.ws.client.transfer.PersonInfoRequest;
import gov.va.ascent.demo.partner.person.ws.client.transfer.PersonInfoResponse;

public interface DemoPersonService {

	/**
	 * Gets the person info.
	 *
	 * @param personInfoRequest A PersonInfoRequest instance
	 * @return A PersonInfoResponse instance
	 */
	PersonInfoResponse getPersonInfo( PersonInfoRequest personInfoRequest);
	
	/**
	 * Gets the person info.
	 *
	 * @param personInfoRequest A PersonInfoRequest instance
	 * @return A PersonInfoResponse instance
	 */
	PersonInfoResponse findPersonByParticipantID( PersonInfoRequest personInfoRequest);
}

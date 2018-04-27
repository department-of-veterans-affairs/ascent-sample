package gov.va.ascent.document.service.api;

import java.util.Map;

import gov.va.ascent.document.service.api.transfer.GetDocumentTypesResponse;
import gov.va.ascent.document.sqs.MessageAttributes;

public interface DocumentService {

	public GetDocumentTypesResponse getDocumentTypes();
	public String getMessageAttributes(String message);
	public Map<String, String> getDocumentAttributes();
	public MessageAttributes getMessageAttributesFromJson(String message);

}

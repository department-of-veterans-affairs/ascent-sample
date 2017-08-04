package gov.va.ascent.demo.service.rest.client.feign;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import gov.va.ascent.document.service.api.transfer.GetDocumentTypesResponse;
import gov.va.ascent.framework.messages.MessageSeverity;

@Component
public class FeignDocumentClientFallback implements FeignDocumentClient {


	@Override
	public ResponseEntity<GetDocumentTypesResponse> getDocumentTypes() {
		// TODO Auto-generated method stub
		GetDocumentTypesResponse response = new GetDocumentTypesResponse();
		response.addMessage(MessageSeverity.FATAL, "DOCUMENT_SERVICE_NOT_AVAILABLE", "This is feign fallback handler, the document service was not available");		
		return new ResponseEntity<GetDocumentTypesResponse>(response, HttpStatus.SERVICE_UNAVAILABLE);
	}
	

}

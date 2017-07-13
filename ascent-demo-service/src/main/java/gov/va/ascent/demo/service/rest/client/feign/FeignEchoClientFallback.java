package gov.va.ascent.demo.service.rest.client.feign;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import gov.va.ascent.demo.service.api.v1.transfer.EchoHostServiceResponse;
import gov.va.ascent.framework.messages.MessageSeverity;

@Component
public class FeignEchoClientFallback implements FeignEchoClient {

	@Override
	public ResponseEntity<EchoHostServiceResponse> echo() {
		EchoHostServiceResponse response = new EchoHostServiceResponse();
		response.addMessage(MessageSeverity.FATAL, "SERVICE_NOT_AVAILABLE", "This is feign fallback handler, the service wasn't available");
		return new ResponseEntity(response, HttpStatus.SERVICE_UNAVAILABLE);
	}

}

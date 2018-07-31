package gov.va.ascent.demo.service.rest.client.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gov.va.ascent.demo.service.config.AscentDocumentServiceFeignConfig;
import gov.va.ascent.document.service.api.transfer.GetDocumentTypesResponse;

@FeignClient(value = "ascent-document-service",
		fallback = FeignDocumentClientFallback.class,
		configuration = AscentDocumentServiceFeignConfig.class)
public interface FeignDocumentClient { // NOSONAR not a functional interface

	@RequestMapping(value = "/document/v1/documentTypes", method = RequestMethod.GET)
	ResponseEntity<GetDocumentTypesResponse> getDocumentTypes();

}

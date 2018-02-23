package gov.va.ascent.document.service.rest.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gov.va.ascent.document.service.api.DocumentService;
import gov.va.ascent.document.service.api.transfer.GetDocumentTypesResponse;
import io.swagger.annotations.ApiOperation;

@RestController
public class DocumentServiceEndPoint implements HealthIndicator {

	private final static Logger LOGGER = LoggerFactory.getLogger(DocumentServiceEndPoint.class);
	
	@Autowired
	@Qualifier("IMPL")
	DocumentService documentService;	

	public static final String URL_PREFIX = "/document/v1";

	@RequestMapping(value = URL_PREFIX + "/health", method = RequestMethod.GET)
	public Health health() {
		// TODO Auto-generated method stub
		return Health.up().withDetail("Document Service REST Endpoint", "Document Service REST Provider Up and Running!").build();
	}
	
	@RequestMapping(value = URL_PREFIX + "/documentTypes", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetDocumentTypesResponse> getDocumentTypes() {
        GetDocumentTypesResponse docResponse = new GetDocumentTypesResponse(); 
		docResponse = documentService.getDocumentTypes();
        LOGGER.info("DOCUMENT SERVICE getDocumentTypes INVOKED");
        return new ResponseEntity<>(docResponse, HttpStatus.OK);
    }		
	

	@PostMapping(value = URL_PREFIX + "/uploadDocument")
    @ApiOperation(value = "Upload a Document",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetDocumentTypesResponse> uploadDocument(final @RequestHeader HttpHeaders headers,
                                      final @RequestBody MultipartFile documentOne
    )
    {
		GetDocumentTypesResponse docResponse = new GetDocumentTypesResponse(); 
		return new ResponseEntity<>(docResponse, HttpStatus.OK);
    }	
}

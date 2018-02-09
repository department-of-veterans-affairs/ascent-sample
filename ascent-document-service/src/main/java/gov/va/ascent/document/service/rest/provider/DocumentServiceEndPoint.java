package gov.va.ascent.document.service.rest.provider;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gov.va.ascent.document.service.api.DocumentService;
import gov.va.ascent.document.service.api.transfer.GetDocumentTypesResponse;
import gov.va.ascent.document.sqs.MessageAttributes;
import gov.va.ascent.framework.swagger.SwaggerResponseMessages;
import gov.va.ascent.starter.aws.autoconfigure.s3.services.S3Services;
import gov.va.ascent.starter.aws.autoconfigure.sqs.services.SQSServices;
import io.swagger.annotations.ApiOperation;

@RestController
public class DocumentServiceEndPoint implements HealthIndicator, SwaggerResponseMessages {

	private final static Logger LOGGER = LoggerFactory.getLogger(DocumentServiceEndPoint.class);
	
	@Autowired
	@Qualifier("IMPL")
	DocumentService documentService;
	
	@Autowired
	S3Services s3Services;
	
	@Autowired
	SQSServices sqsServices;
	    
	@Value("${ascent.s3.uploadfile}")
	private String uploadFilePath;
	    
	@Value("${ascent.s3.key}")
	private String downloadKey;

	public static final String URL_PREFIX = "/document/v1";

	@RequestMapping(value = URL_PREFIX + "/health", method = RequestMethod.GET)
	public Health health() {
		return Health.up().withDetail("Document Service REST Endpoint", "Document Service REST Provider Up and Running!").build();
	}
	
	//@RequestMapping(value = URL_PREFIX + "/uploadStaticFileDocument", method = RequestMethod.GET)
    //public void uploadStaticFileDocument() throws IOException {
	 // System.out.println("---------------- START UPLOAD FILE ----------------");
     // s3Services.uploadFile("ascent-s3-upload-file.txt", uploadFilePath);
      //System.out.println("---------------- START DOWNLOAD FILE ----------------");
      //s3Services.downloadFile(downloadKey);
    //}
	
	@PostMapping(value = URL_PREFIX + "/uploadDocument")
    @ApiOperation(value = "Upload a Document",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> identify(final @RequestHeader HttpHeaders headers,
                                      final @RequestBody MultipartFile documentOne
    )
    {
		Map<String, String> propertyMap = documentService.getDocumentAttributes();
		return s3Services.uploadMultiPartSingle(documentOne, propertyMap );
    }
	
	@PostMapping(value = URL_PREFIX + "/uploadDocumentAndSendMessage")
    @ApiOperation(value = "Upload a Document",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadDocumentAndSendMessage(final @RequestHeader HttpHeaders headers,
                                      final @RequestBody MultipartFile documentOne
    )
    {
		Map<String, String> propertyMap = documentService.getDocumentAttributes();
		s3Services.uploadMultiPartSingle(documentOne, propertyMap);
		LOGGER.info("Sending message {}.", "Sample Test Message");
		String jsonMessage = documentService.getMessageAttributes("Sample Test Message");
		sqsServices.sendMessage(jsonMessage);
		return ResponseEntity.ok().build();
    }	
	
	@PostMapping("/message")
	public ResponseEntity<?> sendMessage(@RequestBody String message) {
	   LOGGER.info("Sending message {}.", message);
	   String jsonMessage = documentService.getMessageAttributes(message);
	   sqsServices.sendMessage(jsonMessage);
	   return ResponseEntity.ok().build();
	}
	
    @JmsListener(destination = "evssstandardqueue")
    public void receive(@Payload String message) {
    		MessageAttributes documentAttributes = documentService.getDocumentAttributesFromJson(message); 
    		String docName = documentAttributes.getDocumentName();
    		s3Services.copyFileFromSourceToTargetBucket(docName);
    		LOGGER.info("Received message {}.", message);
    }
	
	@RequestMapping(value = URL_PREFIX + "/documentTypes", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetDocumentTypesResponse> getDocumentTypes() {
        GetDocumentTypesResponse docResponse = new GetDocumentTypesResponse(); 
		docResponse = documentService.getDocumentTypes();
        LOGGER.info("DOCUMENT SERVICE getDocumentTypes INVOKED");
        return new ResponseEntity<>(docResponse, HttpStatus.OK);
    }	
	
}

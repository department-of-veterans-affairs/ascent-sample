package gov.va.ascent.document.service.rest.provider;

import java.io.IOException;
import java.util.Map;

import javax.jms.TextMessage;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gov.va.ascent.document.service.api.DocumentService;
import gov.va.ascent.document.service.api.transfer.GetDocumentTypesResponse;
import gov.va.ascent.document.service.api.transfer.SubmitPayloadRequest;
import gov.va.ascent.framework.swagger.SwaggerResponseMessages;
import gov.va.ascent.starter.aws.s3.services.S3Service;
import gov.va.ascent.starter.aws.sqs.services.SqsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class DocumentServiceEndPoint implements SwaggerResponseMessages {

  private final static Logger LOGGER = LoggerFactory.getLogger(DocumentServiceEndPoint.class);

  @Autowired
  @Qualifier("IMPL")
  DocumentService documentService;

  @Autowired
  S3Service s3Services;

  @Autowired
  SqsService sqsServices;

  @Value("${ascent.s3.uploadfile}")
  private String uploadFilePath;


  public static final String URL_PREFIX = "/document/v1";


  @RequestMapping(value = URL_PREFIX + "/documentTypes", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GetDocumentTypesResponse> getDocumentTypes() {
    GetDocumentTypesResponse docResponse = new GetDocumentTypesResponse(); 
    docResponse = documentService.getDocumentTypes();
    LOGGER.info("DOCUMENT SERVICE getDocumentTypes INVOKED");
    return new ResponseEntity<>(docResponse, HttpStatus.OK);
  }

  @PostMapping(value = URL_PREFIX + "/uploadDocumentSendMessage")
  @ApiOperation(value = "Upload a Document and Sends Message",
  consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
  produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> uploadDocumentSendMessage(final @RequestHeader HttpHeaders headers,
      final @RequestBody MultipartFile documentOne
      )
  {
    Map<String, String> propertyMap = documentService.getDocumentAttributes();
    s3Services.uploadMultiPartSingle(documentOne, propertyMap);
    LOGGER.info("Sending message {}.", "Sample Test Message");

    String jsonMessage = documentService.getMessageAttributes(documentOne.getOriginalFilename());
    TextMessage textMessage = sqsServices.createTextMessage(jsonMessage);

    sqsServices.sendMessage(textMessage);
    return ResponseEntity.ok().build();
  } 
  
  @PostMapping(value = URL_PREFIX + "/uploadDocumentWithByteArray")
  @ApiOperation(value = "Uploads a Document afetr converting that into a byte array",
  consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
  produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> uploadDocumentWithByteArray(final @RequestHeader HttpHeaders headers,
      final @RequestBody MultipartFile documentOne
      )
  {
    Map<String, String> propertyMap = documentService.getDocumentAttributes();
    try {
		s3Services.uploadByteArray(documentOne.getBytes(), documentOne.getOriginalFilename(), propertyMap);
	} catch (IOException e) {
		LOGGER.error("Error reading bytes: {}", e);
	}
    LOGGER.info("Sending message {}.", "Sample Test Message");

    return ResponseEntity.ok().build();
  } 

  @PostMapping("/message")
  public ResponseEntity<?> sendMessage(@RequestBody String message) {
    LOGGER.info("Sending message {}.", message);
    String jsonMessage = documentService.getMessageAttributes(message);
    TextMessage textMessage = sqsServices.createTextMessage(jsonMessage);
    sqsServices.sendMessage(textMessage);
    return ResponseEntity.ok().build();
  }

  @RequestMapping(path = URL_PREFIX + "/submit", method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  @ApiOperation(value = "Submit a Binary Document and Data",
  consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE,
  produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GetDocumentTypesResponse> submit(@RequestHeader HttpHeaders headers,
      @ApiParam(value = "Document to upload", required = true) @RequestBody byte[] byteFile,
      @ApiParam(value = "Document data",
      required = true) @NotNull @NotEmpty SubmitPayloadRequest submitPayloadRequest) {

    if (LOGGER.isDebugEnabled()) {
      if (submitPayloadRequest != null) {
        LOGGER.debug("SubmitPayloadRequest: {}", submitPayloadRequest.toString());
      }
      if(byteFile!=null) {
        LOGGER.debug("Bytes: {}", byteFile);
        LOGGER.debug("Byte Length: {}", byteFile.length);
      }
    }
    GetDocumentTypesResponse docResponse = new GetDocumentTypesResponse();
    return new ResponseEntity<>(docResponse, HttpStatus.OK);
  }

  @RequestMapping(path = URL_PREFIX + "/submitForm", method = RequestMethod.POST,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ApiOperation(value = "Submit a MultiPart Document and Data",
  consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GetDocumentTypesResponse> submitForm(@RequestHeader HttpHeaders headers,
      @ApiParam(value = "Document to upload",
      required = true) @RequestParam("file") MultipartFile file,
      @ApiParam(value = "Document data",
      required = true) @NotNull @NotEmpty SubmitPayloadRequest submitPayloadRequest) {

    if (LOGGER.isDebugEnabled()) {
      if (submitPayloadRequest != null) {
        LOGGER.debug("SubmitPayloadRequest: {}", submitPayloadRequest.toString());
      }
      if (file != null) {
        LOGGER.debug("MultipartFile: {}", file);
        LOGGER.debug("MultipartFile Size: {}", file.getSize());
        LOGGER.debug("MultipartFile Content Type: {}", file.getContentType());
        LOGGER.debug("MultipartFile Name: {}", file.getName());
        LOGGER.debug("MultipartFile Original Name: {}", file.getOriginalFilename());
      }
      if (headers != null) {
        headers.forEach((k, v) -> {
          LOGGER.debug("Key: " + k + " Value: " + v);
        });
      }
    }
    GetDocumentTypesResponse docResponse = new GetDocumentTypesResponse();
    return new ResponseEntity<>(docResponse, HttpStatus.OK);
  }

}

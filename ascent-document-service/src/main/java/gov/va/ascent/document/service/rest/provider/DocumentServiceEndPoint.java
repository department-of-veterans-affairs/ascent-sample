package gov.va.ascent.document.service.rest.provider;

import javax.validation.constraints.NotNull;

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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.shibboleth.utilities.java.support.annotation.constraint.NotEmpty;

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
    return Health.up().withDetail("Document Service REST Endpoint",
        "Document Service REST Provider Up and Running!").build();
  }

  @RequestMapping(value = URL_PREFIX + "/documentTypes", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GetDocumentTypesResponse> getDocumentTypes() {
    GetDocumentTypesResponse docResponse = new GetDocumentTypesResponse();
    docResponse = documentService.getDocumentTypes();
    LOGGER.info("DOCUMENT SERVICE getDocumentTypes INVOKED");
    return new ResponseEntity<>(docResponse, HttpStatus.OK);
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

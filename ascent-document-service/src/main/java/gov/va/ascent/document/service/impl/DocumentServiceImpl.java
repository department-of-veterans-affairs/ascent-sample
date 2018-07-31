package gov.va.ascent.document.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import gov.va.ascent.document.service.api.DocumentService;
import gov.va.ascent.document.service.api.transfer.DocumentType;
import gov.va.ascent.document.service.api.transfer.GetDocumentTypesResponse;
import gov.va.ascent.document.sqs.MessageAttributes;
import gov.va.ascent.framework.util.SanitizationUtil;

@Service(value = DocumentServiceImpl.BEAN_NAME)
@Component
@Qualifier("IMPL")
@RefreshScope
@DefaultProperties(groupKey = "AscentDocumentDemoServiceGroup")

public class DocumentServiceImpl implements DocumentService {

	/** Constant for the logger for this class */
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentServiceImpl.class);

	@Autowired
	ObjectMapper mapper;

	/** Bean name constant */
	public static final String BEAN_NAME = "documentServiceImpl";

	@Override
	@HystrixCommand(
			fallbackMethod = "getDocumentTypesFallBack",
			commandKey = "GetDocumentTypesCommand",
			ignoreExceptions = { IllegalArgumentException.class })
	public GetDocumentTypesResponse getDocumentTypes() {
		final GetDocumentTypesResponse serviceResponse = new GetDocumentTypesResponse();
		serviceResponse.setDocumentTypes(getListOfDocumentTypes());
		return serviceResponse;
	}

	@HystrixCommand(commandKey = "GetDocumentTypesFallBackCommand")
	public GetDocumentTypesResponse getDocumentTypesFallBack() {
		final GetDocumentTypesResponse serviceResponse = new GetDocumentTypesResponse();
		// NOSONAR TODO: Currently calling the DocumentTypeEnum to get the DocumentType list for fallback
		serviceResponse.setDocumentTypes(getListOfDocumentTypes());
		return serviceResponse;
	}

	private List<DocumentType> getListOfDocumentTypes() {
		return DocumentTypeEnum.getEnumAsDocumentTypeList();
	}

	/**
	 * Populate the metadata for message.
	 * Hard-coding the properties for poc purpose.
	 */
	@Override
	public String getMessageAttributes(final String message) {
		final MessageAttributes documentAttributes = new MessageAttributes();
		documentAttributes.setProcessID("123456789");
		documentAttributes.setUserID("user123");
		documentAttributes.setDocumentID("document123");
		documentAttributes.setDocumentType("userUploaded");
		documentAttributes.setDocumentName("Sample Upload File");
		documentAttributes.setMessage(message);
		try {
			return mapper.writeValueAsString(documentAttributes);
		} catch (final JsonProcessingException e) {
			LOGGER.error("JsonProcessingException {}", e);
			return documentAttributes.toString();
		}

	}

	/**
	 * Populate the metadata for document.
	 * Hard-coding the properties for poc purpose.
	 */
	@Override
	public Map<String, String> getDocumentAttributes() {

		final Map<String, String> propertyMap = new HashMap<>();

		propertyMap.put("processId", "123456789");
		propertyMap.put("userId", "user123");
		propertyMap.put("documentId", "document123");
		propertyMap.put("docType", "user uploaded");
		propertyMap.put("documentName", "Sample Upload File");

		return propertyMap;
	}

	/**
	 * convert the json string into DocumentAttributes object.
	 */
	@Override
	public MessageAttributes getMessageAttributesFromJson(String message) {
		message = SanitizationUtil.stripXSS(message); // NOSONAR intentionally reusing message
		try {
			return mapper.readValue(message, MessageAttributes.class);
		} catch (final JsonParseException e) {
			LOGGER.error("JsonParseException {}", e);
		} catch (final JsonMappingException e) {
			LOGGER.error("JsonMappingException {}", e);
		} catch (final IOException e) {
			LOGGER.error("IOException {}", e);
		}
		return null;
	}

}

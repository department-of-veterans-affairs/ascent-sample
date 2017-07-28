package gov.va.ascent.document.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import gov.va.ascent.document.service.api.DocumentService;
import gov.va.ascent.document.service.api.transfer.DocumentType;
import gov.va.ascent.document.service.api.transfer.GetDocumentTypesResponse;


@Service(value = DocumentServiceImpl.BEAN_NAME)
@Component
@Qualifier("IMPL")
@RefreshScope
@DefaultProperties(groupKey = "AscentDocumentDemoServiceGroup")	

public class DocumentServiceImpl implements DocumentService {


	
	/** Bean name constant */
	public static final String BEAN_NAME = "documentServiceImpl";	
	
	@Override
	@HystrixCommand(
			fallbackMethod = "getDocumentTypesFallBack", 
			commandKey = "GetDocumentTypesCommand", 
			ignoreExceptions = {IllegalArgumentException.class})		
	public GetDocumentTypesResponse getDocumentTypes() {
		// TODO Auto-generated method stub
		GetDocumentTypesResponse serviceResponse = new GetDocumentTypesResponse();
		serviceResponse.setDocumentTypes(getListOfDocumentTypes());
		return serviceResponse;
	}
	
	@HystrixCommand(commandKey = "GetDocumentTypesFallBackCommand")	
	public GetDocumentTypesResponse getDocumentTypesFallBack() {
		GetDocumentTypesResponse serviceResponse = new GetDocumentTypesResponse();
		//TODO:Currently calling the DocumentTypeEnum to get the DocumentType list for fallback
		serviceResponse.setDocumentTypes(getListOfDocumentTypes());
		return serviceResponse;
	}
	
	private List<DocumentType> getListOfDocumentTypes() {
		return DocumentTypeEnum.getEnumAsDocumentTypeList();
	}

}

package gov.va.ascent.document.service.api.transfer;

import java.util.List;

import gov.va.ascent.framework.service.ServiceResponse;

public class GetDocumentTypesResponse extends ServiceResponse {

	/** Generated serial version uid. */
	private static final long serialVersionUID = -3613292399169817507L;
	/** The vba document. */
	private List<DocumentType> documentTypes;

	public List<DocumentType> getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(List<DocumentType> documentTypes) {
		this.documentTypes = documentTypes;
	}

}

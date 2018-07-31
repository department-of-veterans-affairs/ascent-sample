package gov.va.ascent.document.service.impl;

import java.util.ArrayList;
import java.util.List;

import gov.va.ascent.document.service.api.transfer.DocumentType;

public enum DocumentTypeEnum {
	DOCUMENT_TYPE_150("150",
			"VA 21-8056 Request for Retirement Information from the Railroad Retirement Board and Certification of Information From Department of Veterans Affairs",
			"L141", "VeteransClaim", "VeteransClaim"), // NOSONAR enum cannot declare constant ahead of enumeration
	DOCUMENT_TYPE_152("152", "VA 21-8358 Notice to Department of Veterans Affairs of Admission to Uniformed Services Hospital", "L143",
			"VeteransClaim", "VeteransClaim"), // NOSONAR enum cannot declare constant ahead of enumeration
	DOCUMENT_TYPE_153("153", "VA 21-8359 Information Re Veteran in Uniformed Services Hospital or Dispensary", "L144",
			"VeteransClaim", "VeteransClaim"); // NOSONAR enum cannot declare constant ahead of enumeration

	private String docTypeId;
	private String docTypeDesc;
	private String docTypeLabel;
	private String categoryId;
	private String categoryDesc;

	DocumentTypeEnum(final String docTypeId, final String docTypeDesc, final String docTypeLabel, final String categoryId,
			final String categoryDesc) {
		this.docTypeId = docTypeId;
		this.docTypeDesc = docTypeDesc;
		this.docTypeLabel = docTypeLabel;
		this.categoryId = categoryId;
		this.categoryDesc = categoryDesc;
	}

	public String getDocTypeId() {
		return docTypeId;
	}

	public String getDocTypeDesc() {
		return docTypeDesc;
	}

	public String getDocTypeLabel() {
		return docTypeLabel;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public static List<DocumentType> getEnumAsDocumentTypeList() {
		final List<DocumentType> docTypes = new ArrayList<>();
		final DocumentTypeEnum[] docTypeEnums = DocumentTypeEnum.values();
		for (int i = 0; i < docTypeEnums.length; i++) {
			final DocumentType docType = new DocumentType();
			docType.setTypeId(docTypeEnums[i].getDocTypeId());
			docType.setTypeDescription(docTypeEnums[i].getDocTypeDesc());
			docType.setTypeLabel(docTypeEnums[i].getDocTypeLabel());
			docType.setCategoryId(docTypeEnums[i].getCategoryId());
			docType.setCategoryDescription(docTypeEnums[i].getCategoryDesc());
			docTypes.add(docType);
		}

		return docTypes;
	}

}

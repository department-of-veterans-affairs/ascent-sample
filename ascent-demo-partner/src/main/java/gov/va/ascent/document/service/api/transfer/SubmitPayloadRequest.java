/*
 * 
 */
package gov.va.ascent.document.service.api.transfer;

import gov.va.ascent.framework.service.ServiceRequest;

/**
 * A class to represent Submit Payload Request for Documents.
 *
 */
public class SubmitPayloadRequest extends ServiceRequest {

	/** version id. */
	private static final long serialVersionUID = -1348027376496517555L;
	
	/** A String representing a system name. */
    private String systemName;

    /** A String representing a file name. */
    private String fileName;
    
    /** A String representing a doc type. */
    private String docType;
    
    /** A String representing a doc description. */
    private String docDescription;
    
    /** A String representing a doc date. */
    private String docDate;
    
    /** A String representing a claim id. */
    private String claimId;
    
    /** A String representing tracked item ids. */
    private String[] trackedItemIds;
    
	/** A String representing a stamp text. */
	private String stampText;

  /**
   * @return the systemName
   */
  public String getSystemName() {
    return systemName;
  }

  /**
   * @param systemName the systemName to set
   */
  public void setSystemName(String systemName) {
    this.systemName = systemName;
  }

  /**
   * @return the fileName
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * @param fileName the fileName to set
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * @return the docType
   */
  public String getDocType() {
    return docType;
  }

  /**
   * @param docType the docType to set
   */
  public void setDocType(String docType) {
    this.docType = docType;
  }

  /**
   * @return the docDescription
   */
  public String getDocDescription() {
    return docDescription;
  }

  /**
   * @param docDescription the docDescription to set
   */
  public void setDocDescription(String docDescription) {
    this.docDescription = docDescription;
  }

  /**
   * @return the docDate
   */
  public String getDocDate() {
    return docDate;
  }

  /**
   * @param docDate the docDate to set
   */
  public void setDocDate(String docDate) {
    this.docDate = docDate;
  }

  /**
   * @return the claimId
   */
  public String getClaimId() {
    return claimId;
  }

  /**
   * @param claimId the claimId to set
   */
  public void setClaimId(String claimId) {
    this.claimId = claimId;
  }

  /**
   * @return the trackedItemIds
   */
  public String[] getTrackedItemIds() {
    return trackedItemIds;
  }

  /**
   * @param trackedItemIds the trackedItemIds to set
   */
  public void setTrackedItemIds(String[] trackedItemIds) {
    this.trackedItemIds = trackedItemIds;
  }

  /**
   * @return the stampText
   */
  public String getStampText() {
    return stampText;
  }

  /**
   * @param stampText the stampText to set
   */
  public void setStampText(String stampText) {
    this.stampText = stampText;
  }
  
  @Override
  public String toString() {
      return "SubmitPayloadRequest{" +
              "systemName=" + systemName +
              ", fileName='" + fileName + '\'' +
              ", docType='" + docType + '\'' +
              ", docDescription=" + docDescription +
              ", docDate=" + docDate +
              ", claimId=" + claimId +
              ", trackedItemIds=" + trackedItemIds +
               ", stampText=" + stampText +
              '}';
  }

}

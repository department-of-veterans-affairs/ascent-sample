package gov.va.ascent.document.sqs;

public class MessageAttributes {
	private String processID;
	private String userID;
	private String documentID;
	private String documentType;
	private String documentName;
	private String message;
	private int numberOfRetries;
	private long createTimestamp = System.currentTimeMillis();
	
	public String getProcessID() {
		return processID;
	}
	public void setProcessID(String processID) {
		this.processID = processID;
	}
	public String getDocumentID() {
		return documentID;
	}
	public void setDocumentID(String documentID) {
		this.documentID = documentID;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public int getNumberOfRetries() {
		return numberOfRetries;
	}
	public void setNumberOfRetries(int numberOfAttempts) {
		this.numberOfRetries = numberOfAttempts;
	}
	public long getCreateTimestamp() {
      return createTimestamp;
    }
    public void setCreateTimestamp(long createTimestamp) {
      this.createTimestamp = createTimestamp;
    }
}

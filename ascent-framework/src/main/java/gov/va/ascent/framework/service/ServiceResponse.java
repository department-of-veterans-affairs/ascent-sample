package gov.va.ascent.framework.service;

import java.util.LinkedList;
import java.util.List;

import gov.va.ascent.framework.messages.Message;
import gov.va.ascent.framework.messages.MessageSeverity;
import gov.va.ascent.framework.transfer.AbstractTransferObject;

/**
 * A base Response object capable of representing the payload of a service response.
 *
 * @see gov.va.ascent.framework.transfer.AbstractTransferObject
 * @author jshrader
 */
public class ServiceResponse extends AbstractTransferObject {

	private static final long serialVersionUID = -3937937807439785385L;
	
	/** The messages. */
	protected List<Message> messages;

	/**
	 * Instantiates a new rest response.
	 */
	public ServiceResponse() {
		super();
	}
	
	/**
	 * Adds the message.
	 *
	 * @param severity the severity
	 * @param key the key
	 * @param text the text
	 */
	public final void addMessage(final MessageSeverity severity, final String key, final String text){		
		if(messages == null){
			messages = new LinkedList<Message>();
		}
		final Message message = new Message();
		message.setSeverity(severity);
		message.setKey(key);
		message.setText(text);
		messages.add(message);
	}
	
	/**
	 * Adds all messages.
	 *
	 * @param newMessages the newMessages
	 */
	public final void addMessages(final List<Message> newMessages){
		if(messages == null){
			messages = new LinkedList<Message>();
		}
		messages.addAll(newMessages);
	}
	
	/**
	 * Gets the messages.
	 * 
	 * @return the messages
	 */
	public final List<Message> getMessages() {
		if(messages == null){
			messages = new LinkedList<Message>();
		}
		return this.messages;
	}

	/**
	 * Sets the messages.
	 * 
	 * @param messages the new messages
	 */
	public final void setMessages(final List<Message> messages) {
		this.messages = messages;
	}
	
	/**
	 * Checks for messages of type.
	 *
	 * @param severity the severity
	 * @return true, if successful
	 */
	private boolean hasMessagesOfType(final MessageSeverity severity){
		if(getMessages() != null){
			for(Message message: getMessages()){
				if(severity.equals(message.getSeverity())){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks for fatals.
	 *
	 * @return true, if successful
	 */
	public final boolean hasFatals(){
		return hasMessagesOfType(MessageSeverity.FATAL);
	}
	
	/**
	 * Checks for errors.
	 *
	 * @return true, if successful
	 */
	public final boolean hasErrors() {
		return hasMessagesOfType(MessageSeverity.ERROR);
	}
	
	/**
	 * Checks for warnings.
	 *
	 * @return true, if successful
	 */
	public final boolean hasWarnings(){
		return hasMessagesOfType(MessageSeverity.WARN);
	}
	
	/**
	 * Checks for infos.
	 *
	 * @return true, if successful
	 */
	public final boolean hasInfos(){
		return hasMessagesOfType(MessageSeverity.INFO);
	}
	
}

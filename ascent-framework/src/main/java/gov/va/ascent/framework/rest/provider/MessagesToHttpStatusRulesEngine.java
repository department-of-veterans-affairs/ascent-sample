package gov.va.ascent.framework.rest.provider;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import gov.va.ascent.framework.messages.Message;

/**
 * The Class MessagesToHttpStatusRulesEngine is a rather simple rules engine to evaluate Message objects and translate them into
 * HttpStatus return codes. Intended usage is in REST providers that wish to alter the HTTP Response status code based on messages
 * (i.e. FATAL message = 500 response)
 *
 * @author jshrader
 */
public final class MessagesToHttpStatusRulesEngine {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(MessagesToHttpStatusRulesEngine.class);

	/** The Constant NUMBER_OF_MILLIS_N_A_SECOND. */
	private static final int NUMBER_OF_MILLIS_N_A_SECOND = 1000;

	/** The Constant SECS. */
	private static final String SECS = " secs";

	/** The Constant DOT. */
	private static final String DOT = ".";

	/** The collection of rules, will maintain insert order so insert in order you want them evaluated. */
	private final Set<MessagesToHttpStatusRule> rules = new LinkedHashSet<MessagesToHttpStatusRule>();

	/**
	 * Messages to http status.
	 *
	 * @param messagesInResponse the messages in response
	 * @return the integer
	 */
	public HttpStatus messagesToHttpStatus(final List<Message> messagesInResponse) {

		// in debug mode print the time we spend in here
		long startTime = 0;
		if (LOGGER.isDebugEnabled()) {
			startTime = System.currentTimeMillis();
		}

		HttpStatus returnResponse = null;
		if (messagesInResponse != null && !messagesInResponse.isEmpty() && !rules.isEmpty()) {
			returnResponse = evalMessagesAgainstRules(messagesInResponse, rules);
		}

		if (LOGGER.isDebugEnabled()) {
			final long elapsedTime = System.currentTimeMillis() - startTime;
			LOGGER.info("Rules engine execution time: " + elapsedTime / NUMBER_OF_MILLIS_N_A_SECOND + DOT
					+ elapsedTime % NUMBER_OF_MILLIS_N_A_SECOND + SECS);
		}

		return returnResponse;
	}

	/**
	 * Eval messages against rules.  Broken into separate method to appease Sonar.
	 *
	 * @param messagesInResponse the messages in response
	 * @param rules the rules
	 * @return the HttpStatus
	 */
	private static HttpStatus evalMessagesAgainstRules(final List<Message> messagesInResponse, final Set<MessagesToHttpStatusRule> rules) {
		HttpStatus returnResponse = null;
		// convert current messages into Set of Message objects for quicker matching
		final Set<Message> messagesToEval = new HashSet<Message>();
		for (final Message message : messagesInResponse) {
			messagesToEval.add(new Message(message.getSeverity(), message.getKey()));
		}
		// iterate rules, search for matches in our current messages. 1st one found wins, so setup insert order critical
		for (final MessagesToHttpStatusRule rule : rules) {
			final HttpStatus ruleEvalResponse = rule.eval(messagesToEval);
			if (ruleEvalResponse != null) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Rules engine matched rule: " + rule);
				}
				returnResponse = ruleEvalResponse;
				break;
			}
		}
		return returnResponse;
	}

	/**
	 * Adds the rule. Insertion order is maintained so insert in order you desire to be evaluated.
	 *
	 * @param rule the rule
	 */
	public void addRule(final MessagesToHttpStatusRule rule) {
		if (rule != null) {
			rules.add(rule);
		}
	}

}

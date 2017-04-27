package gov.va.ascent.framework.constants;

/**
 * Collection of constants for use with annotations. This will help avoid duplicate literals
 * in the code.
 * 
 * @author jluck
 * @version 1.0
 *
 */
//JSHRADER - supressing the "interface is type" check from checkstyle as we are going to
//			want to store these constants somewhere, and an interface is just as ugly
//			as doing this in a constants class.  Both will fail Sonar, the pattern isn't
//			something we want all over the codebase but in unique situations it is ok.
//CHECKSTYLE:OFF
public interface AnnotationConstants {
	//CHECKSTYLE:ON
	
	//Constants for @SuppressWarnings
	/**
	 * Constants for use with java.lang.SuppressWarnings to
	 * ignore unchecked class casting
	 * @see java.lang.SuppressWarnings
	 */
	String UNCHECKED = "unchecked";
}

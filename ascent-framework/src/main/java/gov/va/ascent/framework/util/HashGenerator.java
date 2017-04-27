package gov.va.ascent.framework.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.util.Assert;

/**
 * Utility class for generating hashes using different algorithms.
 */
public final class HashGenerator {
	
	/** The Constant MD5_RANGE. */
	private static final int MD5_RANGE = 16;

	/** The Constant MD5_0X100. */
	private static final int MD5_0X100 = 0x100;

	/** The Constant MD5_0XFF. */
	private static final int MD5_0XFF = 0xff;
	
	/** The Constant MD5_DIGEST_ALGORITHM. */
	public static final String MD5_DIGEST_ALGORITHM = "MD5";
	
	/** The Constant MD5_HASH_LENGTH. */
	public static final int MD5_HASH_LENGTH = 32;
	
	/**
	 * Constructor to prevent instantiation.
	 */
	private HashGenerator() {
	}
	
	/**
	 * Gets the MD5 hash for input string.
	 *
	 * @param strInput the string input
	 * @return the MD5 hash
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	public static String getMd5ForString(final String strInput) throws NoSuchAlgorithmException {
		MessageDigest digest=MessageDigest.getInstance(MD5_DIGEST_ALGORITHM);
		digest.update(strInput.getBytes());
		byte [] byteData = digest.digest();
		
		StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
        	sBuffer.append(Integer.toString((byteData[i] & MD5_0XFF) + MD5_0X100, MD5_RANGE).substring(1));
        }

        Assert.isTrue(sBuffer.length()==MD5_HASH_LENGTH);
		return sBuffer.toString();
	}

}

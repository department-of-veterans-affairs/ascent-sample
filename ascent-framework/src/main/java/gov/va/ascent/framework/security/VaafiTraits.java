package gov.va.ascent.framework.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gov.va.ascent.framework.security.iam.Gender;
import gov.va.ascent.framework.security.iam.IdType;
import gov.va.ascent.framework.security.iam.PersonDetail;

/**
 * This class contains the VAAFI traits that will be populated in the request headers when a user is authenticating
 * through the VA EAUTH VAAFI infrastructure.
 *
 * @author jshrader
 * @author jluck
 */
public class VaafiTraits extends User {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = -8869670668859283822L;

	/**
	 * The Constant DUMMY_PW.
	 */
	private static final String DUMMY_PW = "n/a";

	/**
	 * DOD EDIPI.
	 */
	private String dodedipnid;

	/**
	 * The credential assurance level per NIST SP800-63.
	 */
	private String assuranceLevel;

	/**
	 * MUST contain given name followed by surname, separated by a single space.
	 */
	private String commonName;

	/**
	 * Method used for authentication (DS Logon, CAC or DFAS).
	 */
	private String authenticationMethod;

	/**
	 * Authority who vetted individual (‘D’-DoD, ‘V’-VA, ‘B’-Both).
	 */
	private String authenticationAuthority;

	/**
	 * Credential Service Provider’s Unique identifier within the federation’s boundaries.
	 */
	private String csid;

	/**
	 * The hash of the userID and applicationID.
	 */
	private String hash;

	/**
	 * date time that the sso authentication occurred.
	 */
	private Date authenticationTimestamp;

	/**
	 * The iv user.
	 */
	private String ivUser;

	/**
	 * The SSO cookie value.
	 */
	private String ssoCookie;

	/**
	 * The email address.
	 */
	private String emailAddress;

	/**
	 * The person detail.
	 */
	private PersonDetail personDetail;

	/**
	 * The auth code from SEP used to make REST calls back to SEP.
	 */
	private String authCode;

	/**
	 * The user id passed in via the VAAFI header.
	 */
	private String uid;

	/**
	 * The icn.
	 */
	private String icn;

	/**
	 * The birlsfilenumber.
	 */
	private String birlsfilenumber;

	/**
	 * The pid.
	 */
	private String pid;

	/**
	 * The cspOnlyFlag.
	 */
	private String cspOnlyFlag;
	
	/*
	 * Section of legacy header values that will be used if the
	 * data is not found in PersonDetails.
	 */

	/**
	 * First name value from legacy header.
	 */
	private String firstName;

	/**
	 * Last name value from legacy header.
	 */
	private String lastName;

	/**
	 * Middle name value from legacy header.
	 */
	private String middleName;

	/**
	 * Date of Birth value from legacy header.
	 */
	private Date dob;

	/**
	 * The pnId value from legacy header
	 */
	private String pnId;

	/**
	 * the pnId type from legacy header
	 */
	private IdType pnIdType;
	
	/*
	 * Vaafi Traits added as part of IAM portal strategy
	 */
	/**
	 * The gender.
	 */
	private Gender gender;

	/**
	 * The security id.
	 */
	private String securityId;

	/**
	 * The prefix.
	 */
	private String prefix;

	/**
	 * The suffix.
	 */
	private String suffix;
	/**
	 * place to store encrypted token so base rest client can send it.
	 */
	private String encryptedToken;

	/**
	 * constructor for json marshalling do not use.
	 */
	public VaafiTraits() {

		super("temp", DUMMY_PW, new ArrayList<GrantedAuthority>());
	}

	/**
	 * Instantiates a new vaafi traits.
	 *
	 * @param username              the username
	 * @param enabled               the enabled
	 * @param accountNonExpired     the account non expired
	 * @param credentialsNonExpired the credentials non expired
	 * @param accountNonLocked      the account non locked
	 * @param authorities           the authorities
	 */
	public VaafiTraits(final String username, final boolean enabled, final boolean accountNonExpired,
			final boolean credentialsNonExpired, final boolean accountNonLocked,
			final Collection<? extends GrantedAuthority> authorities) {
		super(username, DUMMY_PW, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

	/**
	 * Instantiates a new vaafi traits.
	 *
	 * @param username    the username
	 * @param authorities the authorities
	 */
	public VaafiTraits(final String username, final Collection<? extends GrantedAuthority> authorities) {
		super(username, DUMMY_PW, authorities);
	}

	/**
	 * Instantiates a new vaafi traits.
	 *
	 * @param username the username
	 */
	public VaafiTraits(final String username) {
		this(username, new ArrayList<GrantedAuthority>());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.User#toString()
	 */
	@Override
	public final String toString() {
		final String stringValue = ReflectionToStringBuilder.reflectionToString(this);
		// Filter out the SSN value from the string value so PII does not get logged
		return stringValue.replaceAll(",id=[\\d]+,", ",").replaceAll(",pnId=[\\d]+,", ",");
	}

	/**
	 * getter for encrypted token.
	 *
	 * @return encryted
	 */
	@JsonIgnore
	public final String getEncryptedToken() {
		return encryptedToken;
	}

	/**
	 * setter for encrypted token.
	 *
	 * @param encryptedToken token
	 */
	@JsonIgnore
	public final void setEncryptedToken(final String encryptedToken) {
		this.encryptedToken = encryptedToken;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@JsonIgnore
	public final String getId() {
		if (personDetail != null) {
			return personDetail.getId();
		} else {
			return null;
		}
	}

	/**
	 * Gets the id type.
	 *
	 * @return the id type
	 */

	@JsonIgnore
	public final IdType getIdType() {
		if (personDetail != null) {
			return personDetail.getIdType();
		} else {
			return null;
		}
	}

	/**
	 * Gets the assurance level.
	 *
	 * @return the assurance level
	 */
	public final String getAssuranceLevel() {
		return assuranceLevel;
	}

	/**
	 * Sets the assurance level.
	 *
	 * @param assuranceLevel the new assurance level
	 */
	public final void setAssuranceLevel(final String assuranceLevel) {
		this.assuranceLevel = assuranceLevel;
	}

	/**
	 * Gets the common name.
	 *
	 * @return the common name
	 */
	public final String getCommonName() {
		return commonName;
	}

	/**
	 * Sets the common name.
	 *
	 * @param commonName the new common name
	 */
	public final void setCommonName(final String commonName) {
		this.commonName = commonName;
	}

	/**
	 * Gets the authentication method.
	 *
	 * @return the authentication method
	 */
	public final String getAuthenticationMethod() {
		return authenticationMethod;
	}

	/**
	 * Sets the authentication method.
	 *
	 * @param authenticationMethod the new authentication method
	 */
	public final void setAuthenticationMethod(final String authenticationMethod) {
		this.authenticationMethod = authenticationMethod;
	}

	/**
	 * Gets the authentication authority.
	 *
	 * @return the authentication authority
	 */
	public final String getAuthenticationAuthority() {
		return authenticationAuthority;
	}

	/**
	 * Sets the authentication authority.
	 *
	 * @param authenticationAuthority the new authentication authority
	 */
	public final void setAuthenticationAuthority(final String authenticationAuthority) {
		this.authenticationAuthority = authenticationAuthority;
	}

	/**
	 * Gets the dodedipnid.
	 *
	 * @return the dodedipnid
	 */
	public final String getDodedipnid() {
		if (personDetail != null) {
			return personDetail.getEdi();
		} else {
			return dodedipnid;
		}
	}

	/**
	 * Sets the dodedipnid value
	 * <p>
	 * This method should be used for now as the JSON va_eauth_authorization header has not been deployed to production,
	 * this method will be deprecated once that header is production ready.
	 *
	 * @param dodedipnid the new dodedipnid
	 */
	public final void setDodedipnid(final String dodedipnid) {
		this.dodedipnid = dodedipnid;
	}

	/**
	 * Gets the csid.
	 *
	 * @return the csid
	 */
	public final String getCsid() {
		return csid;
	}

	/**
	 * Sets the csid.
	 *
	 * @param csid the new csid
	 */
	public final void setCsid(final String csid) {
		this.csid = csid;
	}

	/**
	 * Gets the hash.
	 *
	 * @return the hash
	 */
	public final String getHash() {
		return hash;
	}

	/**
	 * Sets the hash.
	 *
	 * @param hash the new hash
	 */
	public final void setHash(final String hash) {
		this.hash = hash;
	}

	/**
	 * Gets the sponsordodedipnid.
	 *
	 * @return the sponsordodedipnid
	 */

	@JsonIgnore
	public final String getSponsordodedipnid() {
		String returnValue = null;

		if (personDetail != null) {
			if (personDetail.getHeadOfFamily() != null) {
				returnValue = personDetail.getHeadOfFamily().getEdi();
			} else {
				returnValue = personDetail.getEdi();
			}
		}

		return returnValue;
	}

	/**
	 * Gets the first name. Prefers the value in personDetail, but falls
	 * back to the legacy header value.
	 *
	 * @return the first name
	 */
	public final String getFirstName() {
		if (personDetail != null && personDetail.getFirstName() != null) {
			return personDetail.getFirstName();
		} else {
			return firstName;
		}
	}

	/**
	 * Gets the last name. Prefers the value in personDetail, but falls
	 * back to the legacy header value.
	 *
	 * @return the last name
	 */
	public final String getLastName() {
		if (personDetail != null && personDetail.getLastName() != null) {
			return personDetail.getLastName();
		} else {
			return lastName;
		}
	}

	/**
	 * Gets the middle name. Prefers the value in personDetail, but falls
	 * back to the legacy header value.
	 *
	 * @return the middle name
	 */
	public final String getMiddleName() {
		if (personDetail != null && personDetail.getMiddleName() != null) {
			return personDetail.getMiddleName();
		} else {
			return middleName;
		}
	}

	/**
	 * Gets the gender.
	 *
	 * @return the gender
	 */
	public final Gender getGender() {
		if (personDetail != null) {
			return personDetail.getGender();
		} else {
			return gender;
		}
	}

	/**
	 * Sets the gender.
	 *
	 * @param gender the new gender
	 */
	public final void setGender(final Gender gender) {
		this.gender = gender;
	}

	/**
	 * Gets the ssn.
	 *
	 * @return the ssn
	 */
	@JsonIgnore
	public final String getSsn() {
		if (personDetail != null && IdType.SSN.equals(personDetail.getIdType())) {
			return personDetail.getId();
		} else if (IdType.SSN.equals(pnIdType)) {
			return pnId;
		} else {
			return null;
		}
	}

	/**
	 * Gets the dob. Prefers the value in personDetail, but falls
	 * back to the legacy header value.
	 *
	 * @return the dob
	 */

	public final Date getDob() {
		if (personDetail != null && personDetail.getBirthDate() != null) {
			return personDetail.getBirthDate();
		} else if (dob != null) {
			return new Date(dob.getTime());
		} else {
			return null;
		}
	}

	/**
	 * Gets the authentication timestamp.
	 *
	 * @return the authentication timestamp
	 */
	public final Date getAuthenticationTimestamp() {
		if (authenticationTimestamp == null) {
			return null;
		} else {
			return new Date(authenticationTimestamp.getTime());
		}
	}

	/**
	 * Sets the authentication timestamp.
	 *
	 * @param authenticationTimestamp the new authentication timestamp
	 */
	public final void setAuthenticationTimestamp(final Date authenticationTimestamp) {
		if (authenticationTimestamp == null) {
			this.authenticationTimestamp = null;
		} else {
			this.authenticationTimestamp = new Date(authenticationTimestamp.getTime());
		}
	}

	/**
	 * Gets the iv user.
	 *
	 * @return the iv user
	 */
	public final String getIvUser() {
		return ivUser;
	}

	/**
	 * Sets the iv user.
	 *
	 * @param ivUser the new iv user
	 */
	public final void setIvUser(final String ivUser) {
		this.ivUser = ivUser;
	}

	/**
	 * Gets the SSO cookie value.
	 *
	 * @return the sso cookie value
	 */
	public final String getSsoCookie() {
		return ssoCookie;
	}

	/**
	 * Sets the SSO cookie value.
	 *
	 * @param ssoCookie the sso cookie value
	 */
	public final void setSsoCookie(final String ssoCookie) {
		this.ssoCookie = ssoCookie;
	}

	/**
	 * Gets the pnId.
	 *
	 * @return the pnId
	 */
	public final String getPnId() {
		return pnId;
	}

	/**
	 * Sets the pnId.
	 *
	 * @param pnId the pnId
	 */
	public final void setPnId(final String pnId) {
		this.pnId = pnId;
	}

	/**
	 * Gets the email address.
	 *
	 * @return the email address
	 */
	public final String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Sets the email address.
	 *
	 * @param emailAddress the email address
	 */
	public final void setEmailAddress(final String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * Gets the person detail.
	 *
	 * @return the person detail
	 */
	public final PersonDetail getPersonDetail() {
		return personDetail;
	}

	/**
	 * Sets the person detail.
	 *
	 * @param personDetail the new person detail
	 */
	public final void setPersonDetail(final PersonDetail personDetail) {
		this.personDetail = personDetail;
	}

	/**
	 * Gets the auth code.
	 *
	 * @return the auth code
	 */
	public final String getAuthCode() {
		return authCode;
	}

	/**
	 * Sets the auth code.
	 *
	 * @param authCode the auth code
	 */
	public final void setAuthCode(final String authCode) {
		this.authCode = authCode;
	}

	/**
	 * Gets the UID.
	 *
	 * @return the UID
	 */
	public final String getUid() {
		return uid;
	}

	/**
	 * Sets the UID.
	 *
	 * @param uid the uid
	 */
	public final void setUid(final String uid) {
		this.uid = uid;
	}

	/**
	 * Sets the first name.
	 *
	 * @param firstName the new first name
	 */
	public final void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Sets the last name.
	 *
	 * @param lastName the new last name
	 */
	public final void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Sets the middle name.
	 *
	 * @param middleName the new middle name
	 */
	public final void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	/**
	 * Sets the dob.
	 *
	 * @param dob the new dob
	 */
	public final void setDob(final Date dob) {
		if (dob != null) {
			this.dob = new Date(dob.getTime());
		} else {
			this.dob = null;
		}
	}

	/**
	 * Gets the pn id type.
	 *
	 * @return the pn id type
	 */
	public final IdType getPnIdType() {
		return pnIdType;
	}

	/**
	 * Sets the pn id type.
	 *
	 * @param pnIdType the new pn id type
	 */
	public final void setPnIdType(final IdType pnIdType) {
		this.pnIdType = pnIdType;
	}

	/**
	 * Gets the security id.
	 *
	 * @return the security id
	 */
	public final String getSecurityId() {
		return securityId;
	}

	/**
	 * Sets the security id.
	 *
	 * @param securityId the new security id
	 */
	public final void setSecurityId(final String securityId) {
		this.securityId = securityId;
	}

	/**
	 * Gets the prefix.
	 *
	 * @return the prefix
	 */
	public final String getPrefix() {
		return prefix;
	}

	/**
	 * Sets the prefix.
	 *
	 * @param prefix the new prefix
	 */
	public final void setPrefix(final String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Gets the suffix.
	 *
	 * @return the suffix
	 */
	public final String getSuffix() {
		return suffix;
	}

	/**
	 * Sets the suffix.
	 *
	 * @param suffix the new suffix
	 */
	public final void setSuffix(final String suffix) {
		this.suffix = suffix;
	}

	/**
	 * Gets the icn.
	 *
	 * @return the icn
	 */
	public final String getIcn() {
		return icn;
	}

	/**
	 * Sets the icn.
	 *
	 * @param icn the new icn
	 */
	public final void setIcn(final String icn) {
		this.icn = icn;
	}

	/**
	 * Gets the birlsfilenumber.
	 *
	 * @return the birlsfilenumber
	 */
	public final String getBirlsfilenumber() {
		return birlsfilenumber;
	}

	/**
	 * Sets the birlsfilenumber.
	 *
	 * @param birlsfilenumber the new birlsfilenumber
	 */
	public final void setBirlsfilenumber(final String birlsfilenumber) {
		this.birlsfilenumber = birlsfilenumber;
	}

	/**
	 * Gets the pid.
	 *
	 * @return the pid
	 */
	public final String getPid() {
		return pid;
	}

	/**
	 * Sets the pid.
	 *
	 * @param pid the new pid
	 */
	public final void setPid(final String pid) {
		this.pid = pid;
	}

	/**
	 * Gets the cspOnlyFlag.
	 *
	 * @return the cspOnlyFlag
	 */
	public final String getCspOnlyFlag() {
		return cspOnlyFlag;
	}

	/**
	 * Sets the cspOnlyFlag.
	 *
	 * @param cspOnlyFlag the new cspOnlyFlag
	 */
	public final void setCspOnlyFlag(final String cspOnlyFlag) {
		this.cspOnlyFlag = cspOnlyFlag;
	}

	/**
	 * used to make json not try and set this.
	 *
	 * @return granted authorities
	 */
	@Override
	@JsonIgnore
	public final Collection<GrantedAuthority> getAuthorities() {
		return super.getAuthorities();
	}
}

package gov.va.ascent.framework.config;

/**
 * Constants to store the profiles we commonly used in WSS applications.
 *
 * @author jshrader
 */
// JSHRADER - supressing the "interface is type" check from checkstyle as we are going to
// want to store these constants somewhere, and an interface is just as ugly
// as doing this in a constants class. Both will fail Sonar, the pattern isn't
// something we want all over the codebase but in some situations it is ok.
// CHECKSTYLE:OFF
public interface AscentCommonSpringProfiles {
	// CHECKSTYLE:ON

	/**
	 * Spring default profile
	 */
	String PROFILE_DEFAULT = "default";

	/**
	 * Spring profile for local dev environment
	 */
	String PROFILE_ENV_LOCAL_DEV = "local_dev";

	/**
	 * Spring profile for local dev integration environment
	 */
	String PROFILE_ENV_LOCAL_DEVINT = "local_devint";

	/**
	 * Spring profile for local dev environment
	 */
	String PROFILE_ENV_DEV_VM = "dev_vm";

	/**
	 * Spring profile for iw dev environment
	 */
	String PROFILE_ENV_IW_DEV = "iw_dev";

	/**
	 * Spring profile for aws_ci environment
	 */
	String PROFILE_ENV_AWS_CI = "aws_ci";

	/**
	 * Spring profile for int environment
	 */
	String PROFILE_ENV_INT = "int";

	/**
	 * Spring profile for cv environment
	 */
	String PROFILE_ENV_CV = "cv";

	/**
	 * Spring profile for qa environment
	 */
	String PROFILE_ENV_QA = "qa";

	/**
	 * Spring profile for qa2 environment
	 */
	String PROFILE_ENV_QA2 = "qa2";

	/**
	 * Spring profile for pint environment
	 */
	String PROFILE_ENV_PINT = "pint";

	/**
	 * Spring profile for pint2 environment.
	 */
	String PROFILE_ENV_PINT2 = "pint2";

	/**
	 * Spring profile for perf environment.
	 */
	String PROFILE_ENV_PERF = "perf";

	/**
	 * Spring profile for demo environment.
	 */
	String PROFILE_ENV_DEMO = "demo";

	/**
	 * Spring profile for preprod environment
	 */
	String PROFILE_ENV_PREPROD = "preprod";

	/**
	 * Spring profile for preprod2 environment
	 */
	String PROFILE_ENV_PREPROD2 = "preprod2";

	/**
	 * Spring profile for prod environment
	 */
	String PROFILE_ENV_PROD = "prod";

	/**
	 * Spring profile for 2nd prod environment
	 */
	String PROFILE_ENV_PROD_2 = "prod2";

	/**
	 * Spring profile for remote client real implementations
	 */
	String PROFILE_REMOTE_CLIENT_IMPLS = "remote_client_impls";

	/**
	 * Spring profile for remote client simulator implementations
	 */
	String PROFILE_REMOTE_CLIENT_SIMULATORS = "remote_client_sims";
	/**
	 * Spring profile for unit test specific impls
	 */
	String PROFILE_UNIT_TEST = "unit_test_sims";

	/**
	 * Spring profile for remote iam logoff simulator implementations
	 */
	String PROFILE_REMOTE_IAM_LOGOFF_SIMULATORS = "iam_logoff_client_sims";

	/**
	 * Spring profile for remote iam logoff impl implementations
	 */
	String PROFILE_REMOTE_IAM_LOGOFF_IMPLS = "iam_logoff_client_impl";

	/**
	 * Spring profile for remote audit simulator implementations
	 */
	String PROFILE_REMOTE_AUDIT_SIMULATORS = "remote_audit_client_sims";
	/**
	 * Spring profile for remote audit impl implementations
	 */
	String PROFILE_REMOTE_AUDIT_IMPLS = "remote_audit_client_impl";

	/**
	 * Spring profile for remote iam logoff simulator implementations
	 */
	String PROFILE_REMOTE_JSON_CREATION_SIMULATOR = "json_creation_client_sim";

	/**
	 * Spring profile for remote iam logoff impl implementations
	 */
	String PROFILE_REMOTE_JSON_CREATION_IMPL = "json_creation_client_impl";

}

@Library('ascent@log-publish') _

microservicePipeline {

    dockerBuilds = [
        "ascent/demo-service": "ascent-demo-service",
        "ascent/document-service": "ascent-document-service"
    ]

    /******  Testing *******/

    /*
    Define a mapping of environment variables that will be populated with Vault token values
    from the associated vault token role
    */
    vaultTokens = [
        "VAULT_TOKEN": "ascent-demo",
        "PLATFORM_VAULT_TOKEN": "ascent-platform",
        "REDIS_VAULT_TOKEN" : "redis",
    ]

    //Test Environment Definition
    testEnvironment = ['docker-compose.yml', 'docker-compose.override.yml']

    //Name of the service to test
    serviceToTest = 'ascent-gateway'

    //Directory containing JSON files for Cucumber reports
    cucumberReportDirectory = 'vetservices-refdata-inttest/target'
    cucumberOpts = '--tags @DEV'
    containerPort = 8080

    /********* Performance Testing ************/

    perfEnvironment = ['docker-compose.yml']
    //perfTestOptions = ""

    /*********  Deployment Configuration ***********/
    stackName = "sample"
    serviceName = "demo-service"
}
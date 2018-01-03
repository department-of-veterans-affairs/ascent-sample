@Library('ascent') _

microservicePipeline {

    //Specify string of comma separated upstream projects that will
    //trigger this build if successful
    upstreamProjects = '../ascent-platform/development'
    
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
        "DEMO_SERVICE_VAULT_TOKEN": "ascent-demo",
        "PLATFORM_VAULT_TOKEN": "ascent-platform"
    ]

    //Test Environment Definition
    testEnvironment = ['test-env.yml']

    //Name of the service to test
    serviceToTest = 'ascent-gateway'

    //Directory containing JSON files for Cucumber reports
    cucumberReportDirectory = 'ascent-demo-service-inttest/target'
}
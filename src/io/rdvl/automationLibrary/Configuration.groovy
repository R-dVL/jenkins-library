package io.rdvl.automationLibrary

import groovy.json.JsonSlurper

class Configuration {
    // Default params
    private def configuration
    private String configurationPath

    Configuration () {
        // Parse configuration json
        def jsonSlurper = new JsonSlurper()
        this.configurationPath = "/automation-lab/resources/configuration.json"
        this.configuration = jsonSlurper.parse(new File(configurationPath))
    }

    @NonCPS
    def getConfiguration() {
        return this.configuration
    }

    @Override
    @NonCPS
    public String toString() {
        return "JSON: ${configuration}"
    }
}
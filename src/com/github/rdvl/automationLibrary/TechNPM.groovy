package com.github.rdvl.automationLibrary;

public class TechNPM {
    // Pipeline Context
    private def pipeline

    // Tech
    private String name
    private String version
    private String artifactId
    private String nexusRepository
    private String url
    private def destination
    private String tech

    TechNPM(pipeline, name, version, artifactId, nexusRepository, url, destination) {
        this.pipeline = pipeline
        this.name = name
        this.version = version
        this.artifactId = artifactId
        this.nexusRepository = nexusRepository
        this.url = url
        this.destination = destination
    }

    def prepare() {
        // Download Code
        pipeline.checkout(scm: [$class: 'GitSCM', userRemoteConfigs: [[url: url, credentialsId: 'github-login-credentials']], branches: [[name: version]]],poll: false)

        pipeline.sh("npm config set //npm.pkg.github.com/:_authToken=${pipeline.github_token}")
        // Config .npmrc file
        pipeline.sh("npm publish")
    }

    def deploy() {
        pipeline.host.sshCommand("""mkdir -p ${name}/${artifactId}
        cd ${name}/${artifactId}
        curl -O -L https://_:${pipeline.github_token}@npm.pkg.github.com/R-dVL/${name}/packages/${artifactId}/${version}/${artifactId}.tgz
        """)
    }

    @Override
    @NonCPS
    public String toString() {
        return """
            Name: ${name}
            Version: ${version}
            Artifact: ${artifactId}
            Url: ${url}
            Destination: ${destination}
            Tech: ${tech}
        """;
    }
}

package com.github.rdvl.automationLibrary

def call() {
    node {
        // Environment variables
        environment {
            cfg
        }
        // Pipeline error control
        try {
            // Configuration instance
            cfg = Configuration.getInstance()
            // Default Params
            Host host = new Host(this, HOST)

            stage('Host Setup') {
                // Retrieve info from Jenkins
                script {
                    // User & Password
                    withCredentials([
                        usernamePassword(credentialsId: host.getConfigCredentials(), usernameVariable: 'user', passwordVariable: 'password')]) {
                            host.setUser(user)
                            host.setPassword(password)
                    }
                    // IP
                    withCredentials([
                        string(credentialsId: host.getConfigIp(), variable: 'ip')]) {
                            host.setIp(ip)
                    }
                }
            }

            stage('Execute Command') {
                host.sshCommand(CMD)
            }

            stage('Finished msg'){
                withCredentials(([string(credentialsId: 'telegram-bot-token', variable: 'TOKEN'),
                string(credentialsId: 'telegram-chat-id', variable: 'CHAT_ID')])) {
                    sh ('curl -s -X POST https://api.telegram.org/bot$TOKEN/sendMessage -d "chat_id=$CHAT_ID"  -d text="[✅] Build successfully 😊"')
                }
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error(err.getMessage())
        }
    }
}
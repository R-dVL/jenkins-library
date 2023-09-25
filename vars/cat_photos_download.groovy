package com.rdvl.automationLibrary

import java.time.LocalDate

def call() {
    node {
        // Environment variables
        environment {
            cfg
            host
        }
        // Pipeline error control
        try {
            // Configuration instance
            cfg = Configuration.getInstance()
            // Default Params
            host = new Host(this, HOST)
            print(host)
            LocalDate date = LocalDate.now()

            // Stages
            stage('Download Photos') {
                script {
                    def cats = []
                    def not_cats = []

                    def response = sh(script: "curl http://192.168.1.55:3001/photos/date/${date} --output photos.json", returnStdout: true).trim()
                    def photos = readJSON file: 'photos.json'

                    photos.each { photo ->
                        if(photo.cat) {
                            cats.add(photo.image)
                        } else {
                            not_cats.add(photo.image)
                        }
                    }

                    dir('cats') {
                        def count = 0
                        for(image in cats) {
                            if(image != null) {
                                def file = "${date}_cat_${count}.jpg"
                                writeFile file: file, text: image, encoding: 'Base64'
                                host.sshPut(file, '/home/jenkins/cat-watcher/dataset/cats')
                                count += 1
                                print("CATS | ${count} of ${cats.size()}")
                            }
                        }
                    }

                    dir('not_cats') {
                        def count = 0
                        for(image in not_cats) {
                            if(image != null) {
                                def file = "${date}_cat_${count}.jpg"
                                writeFile file: file, text: image, encoding: 'Base64'
                                host.sshPut(file, '/home/jenkins/cat-watcher/dataset/not_cats')
                                count += 1
                                print("NOT CATS | ${count} of ${not_cats.size()}")
                            }
                        }
                        print("Cat files written: ${count}")
                    }
                }
            }

        } catch(Exception e) {
            println("ALERT | Something went wrong")
            error(e.getMessage())
        }
    }
}
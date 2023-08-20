package com.github.rdvl.automationLibrary

class Configuration {
    // Singleton class to store configuration
    private static Configuration instance

    // Urls
    static final String urlRepo = 'https://github.com/R-dVL/automationLibrary.git'

    // Hosts
    static final def hosts = [
        'server': [
            'credentials': 'server-credentials',
            'ip': 'server-ip'
            ],
        'rpi': [
            'credentials': 'rpi-credentials',
            'ip': 'rpi-ip'
        ]
    ]

    // Projects
    static final def projects = [
        'cat-watcher': [
            'url': 'https://github.com/R-dVL/cat-watcher.git',
            'destination': '/home/rdvl/cat-watcher',
            'tech': 'maven'
        ],

        'lima-backend': [
            'url': 'https://github.com/R-dVL/lima-backend.git',
            'destination': '/home/rdvl/lima-backend',
            'tech': 'node'
        ],

        'lima-frontend': [
            'url': 'https://github.com/R-dVL/lima-frontend.git',
            'destination': '/home/rdvl/lima-frontend',
            'tech': 'react'
        ]
    ]

    // Singleton constructor
    private Configuration () {}

    @NonCPS
    static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration()
        }
        return instance
    }

    @Override
    @NonCPS
    public String toString() {
        return """
            Repository URL: ${urlRepo}
            Hosts: ${hosts}
        """
    }
}
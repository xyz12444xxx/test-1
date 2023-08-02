package config

import config.jfrog.JfrogBase


JfrogBase jfrogBase

pipeline {
    agent any
    parameters {
        credentials (
            name: 'artifactory_server_url',
            description: 'Artifactory server url',
            defaultValue: 'jfrog_server',
            credentialType: 'Secret Text',
            required: true
        )
        credentials (
            name: 'artifactory_cred_id',
            description: 'Artifactory credentials id',
            defaultValue: 'jfrog_credentials',
            credentialType: 'Username with password',
            required: true
        )
    }
    stages {
        stage('Initiate') {
            steps {
                echo 'Initiating....'
                script {
                    // create artifactory server
                    initiate()
                }
            }
        }
        stage('Build') {
            steps {
                echo 'Building..'
                sh 'python3 --version'
                sh 'python3 src/main.py'                
            }
        }
        // stage('Download') {            
        //     // otherwise get the log from artifactory
        //     steps {
        //         script {
        //             server.download(jfrogSpec)
        //         }
        //     }
        // }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
        stage('Archive') {
            steps {
                echo 'Archiving....'
                script {
                    // upload reports to artifactory
                    jfrogBase.UploadReports('target', ['*.log'])
                }
                
                echo 'Archiving done....'
            }
        }
    }
}

void initiate() {
    // create artifactory server
    try {
        jfrogBase = new JfrogBase('artifactory-1', params.artifactory_server_url, params.artifactory_cred_id, 'logs')
    } catch (Exception e) {
        error "Failed to create Artifactory server"
    }
}
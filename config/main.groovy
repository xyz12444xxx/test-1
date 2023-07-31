// import jfrog
import jfrog.dsl.*

// get the jfrog server details
def server = Artifactory.server 'jfrog-1'

// get the credentials from the jenkins credentials store
def credentials = Artifactory.credentials 'jfroguser'

// get the jfrog repository details
def repo = Artifactory.repo 'demo-thales'

def uploadSpec = """{
    "files": [
        {
            "pattern": "target/*.log",
            "target": "logs/"
        }
    ]
}"""

pipeline {
    agent any
    // tools {
    // }
    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                sh 'python3 --version'
                sh 'python3 src/main.py'                
            }
        }
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

                // get the file that is generated in the build stage under the target folder
                archiveArtifacts artifacts: 'target/*.log', fingerprint: true

                scripts {
                    // upload the file to the jfrog repository
                    upload spec: uploadSpec, server: server, credentials: credentials, failNoOp: true
                }
                
                echo 'Archiving done....'
            }
        }
    }
}
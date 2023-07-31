// import jfrog
import jfrog.dsl.*
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
                    storeToArtifactory()
                }
                
                echo 'Archiving done....'
            }
        }
    }
}

def storeToArtifactory() {
    def server = Artifactory.server 'artifactory'
    def uploadSpec = """{
        "files": [
            {
                "pattern": "target/*.log",
                "target": "generic-local/"
            }
        ]
    }"""
    server.upload(uploadSpec)
}
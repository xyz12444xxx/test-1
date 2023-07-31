// import jfrog
import jfrog.dsl.*

// get the jfrog server details
def server = Artifactory.server 'jfrog-1'

// get the credentials from the jenkins credentials store
def credentials = Artifactory.credentials 'jfroguser'

// get the jfrog repository details
def repo = Artifactory.repo 'demo-thales'

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
                
                // upload to jfrog artifactory
                server.upload fileSpec: '''{
                    "files": [
                        {
                            "pattern": "target/*.log",
                            "target": "demo-thales/"
                        }
                    ]
                }''', failNoOp: true, failNoFilesDeploy: true, failBuild: true, buildName: 'demo-thales', buildNumber: '1.0.0'

                echo 'Archiving done....'
            }
        }
    }
}
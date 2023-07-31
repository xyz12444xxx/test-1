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
                // store the artifacts in jfrog http://localhost:8082/artifactory/demo-thales/
                storeArtifacts allowEmptyArchive: true, allowUnstable: true, excludes: '', fingerprint: true, includes: 'target/*.log', targetRepo: 'demo-thales', uploadPattern: '' 

                echo 'Archiving done....'
            }
        }
    }
}
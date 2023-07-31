def server = Artifactory.server 'jfrog-1'
def jfrogSpec = """{
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
            // only build if there are changes in py file types
            when {
                changeset "**/*.py"
            }
            steps {
                echo 'Building..'
                sh 'python3 --version'
                sh 'python3 src/main.py'                
            }
        }
        stage('Download') {            
            // otherwise get the log from artifactory
            when {
                not {
                    changeset "**/*.py"
                }
            }
            steps {
                script {
                    server.download(jfrogSpec)
                }
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

                script {
                    server.upload(jfrogSpec)
                }
                
                echo 'Archiving done....'
            }
        }
    }
}
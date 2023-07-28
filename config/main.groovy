pipeline {
    agent any
    tools {
        maven 'Maven 3.6.3'
    }
    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                // check if mvn is installed
                sh 'mvn --version'
                // if not, then install
                sh 'mvn clean install'
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
    }
}
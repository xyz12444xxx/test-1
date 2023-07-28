pipeline {
    agent any
    // tools {
    // }
    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                sh 'python --version'
                // run myfile.py from workspace
                sh 'python myfile.py'                
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
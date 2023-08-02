def m = [:]

pipeline {
    agent any
    parameters {
        string (
            name: 'artifactory_server_url',
            description: 'Artifactory server url',
            defaultValue: 'http://"172.21.0.2:8082/artifactory/demo-work/',
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
    // setup modules

    // create artifactory server    
    try {
        def jfrogBase = load 'config/jfrog/jfrog.gvy'
        m.jfrogBase = jfrogBase.GetInstance('artifactory-1', params.artifactory_server_url, params.artifactory_cred_id, 'logs')
    } catch (Exception e) {
        echo "Failed to create Artifactory server-echo ${e}"
        error "Failed to create Artifactory server"
    }
}
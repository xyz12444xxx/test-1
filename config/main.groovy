jfrog = evaluate(readTrusted('config/jfrog/JfrogBase.groovy'))

pipeline {
    agent any
    parameters {
        string (
            name: 'artifactory_server_url',
            description: 'Artifactory server url',
            defaultValue: 'http://172.21.0.2:8082/artifactory/demo-work'
        )
        string (
            name: 'artifactory_repo',
            description: 'Artifactory repository',
            defaultValue: 'demo-work'
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
                // get the file that is generated in the build stage under the target folder
                archiveArtifacts artifacts: 'target/*.log', fingerprint: true
                script {
                    // upload reports to artifactory
                    // rtUpload (
                    //     serverId: 'artifactory-1',
                    //     spec: """{
                    //         "files": [
                    //             {
                    //                 "pattern": "target/*.log",
                    //                 "target": "logs/"
                    //             }
                    //         ]
                    //     }"""
                    // )
                    jfrog.UploadReports('target', (String[])['baseline_1.0.0.log'])
                }
                
                echo 'Archiving done....'
            }
        }
    }
}

void initiate() {
    // create artifactory server
    try {
        jfrog.init('artifactory-2', params.artifactory_server_url, params.artifactory_repo, params.artifactory_cred_id, 'logs')
    } catch (Exception e) {
        echo "Failed to create Artifactory server-echo ${e}"
        error "Failed to create Artifactory server"
    }
}
// _instance = null
private String serverUrl
private String repo
private String credentialsId
private String reportsStorePath

def init(String id, String serverUrl, String repo, String credentialsId, String reportsStorePath) {
    this.serverUrl = serverUrl
    this.repo = repo
    this.credentialsId = credentialsId
    this.reportsStorePath = reportsStorePath
    node {
        // with credentials to get username and password from the jenkins credentials store
        withCredentials([usernamePassword(credentialsId: credentialsId, passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
            // call curl to check if server is up in a shell script
            def server = sh(script: "curl -k ${this.serverUrl}/api/system/ping -u " + '$USERNAME:$PASSWORD', returnStdout: true).trim()
            // do not show password in the variable
            if (server != 'OK') {
                echo "Server ${this.serverUrl} is not up"
            }
        }
    }
}

def uploadReports(String fromDir, String[] filenames) {
    withCredentials([usernamePassword(credentialsId: credentialsId, passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
        // pwd and show files
        sh "pwd"
        sh "ls -l"
        sh "ls -l ${fromDir}"
        for (String filename : filenames) {
            def result = sh(script: "curl -k -T ${fromDir}/${filename} ${this.serverUrl}/artifactory/${this.repo}/${this.reportsStorePath}/${filename} -u " + '$USERNAME:$PASSWORD', returnStdout: true).trim()
            echo "${result}"
        }
    }
}

class JfrogBase {
    public String id
    public String serverUrl
    public String credentialsId

    public String repo
    public String reportsStorePath

    // constructor
    JfrogBase(String id, String serverUrl, String repo, String credentialsId, String reportsStorePath) {
        this.id = id
        this.serverUrl = serverUrl
        this.repo = repo
        this.credentialsId = credentialsId
        this.reportsStorePath = reportsStorePath
    }
}

return this
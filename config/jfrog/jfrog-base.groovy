_instance = null

def init(String id, String serverUrl, String repo, String credentialsId, String reportsStorePath) {
    node {
        // call curl to check if server is up in a shell script
        def serverUp = sh(script: "curl -s -o /dev/null -w \"%{http_code}\" ${serverUrl}api/system/ping", returnStdout: true).trim()
        if (serverUp != '200') {
            echo 'Artifactory server is not up'
        }
    }
}

def uploadReports(String fromDir, String[] filenames) {
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
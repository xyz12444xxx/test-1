_instance = null

def init(String id, String serverUrl, String repo, String credentialsId, String reportsStorePath) {
    node {
        // call curl to check if server is up in a shell script
        def serverUp = sh(script: "curl -k ${serverUrl}/api/system/ping -u ${credentialsId}", returnStdout: true).trim()
        echo "server: ${serverUp}"
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
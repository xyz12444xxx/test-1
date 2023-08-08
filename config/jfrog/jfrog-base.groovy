_instance = null

def init(String id, String serverUrl, String repo, String credentialsId, String reportsStorePath) {
    if (!_instance) {
        _instance = new JfrogBase(id, serverUrl, repo, credentialsId, reportsStorePath, 
        Artifactory.newServer(url: serverUrl, credentialsId: credentialsId))

        sh "curl ${serverUrl}"
    } else {
        echo 'instance already exists'
    }
}

def uploadReports(String fromDir, String[] filenames) {
    if (!_instance) {
        throw new Exception("JfrogBase instance not initialized")
    }

    for (filename in filenames) {
        _instance.server.upload spec: """{
            "files": [
                {
                    "pattern": "${fromDir}/${filename}",
                    "target": "${_instance.repo}/${_instance.reportsStorePath}/"
                }
            ]
        }"""
    }
}

class JfrogBase {
    private String id
    private String serverUrl
    private String credentialsId

    public String repo
    public String reportsStorePath
    def server = null

    // constructor
    JfrogBase(String id, String serverUrl, String repo, String credentialsId, String reportsStorePath, def server) {
        this.id = id
        this.serverUrl = serverUrl
        this.repo = repo
        this.credentialsId = credentialsId
        this.reportsStorePath = reportsStorePath
        this.server = server
    }
}

return this
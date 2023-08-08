_instance = null

def init(String id, String serverUrl, String repo, String credentialsId, String reportsStorePath) {
    if (!_instance) {
        _instance = new JfrogBase(id, serverUrl, repo, credentialsId, reportsStorePath)
}

def uploadReports(String fromDir, String[] filenames) {    
    // use REST API from artifactory to check if server is up
    def url = new URL(serverUrl + 'api/system/ping')
    def connection = url.openConnection()
    withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        connection.setRequestProperty("Authorization", "Basic " + "${USERNAME}:${PASSWORD}".bytes.encodeBase64().toString())
    }

    connection.connect()
    if (connection.responseCode != 200) {
        echo 'Artifactory server is not up'
    }

    echo 'Artifactory server is up'
}

class JfrogBase {
    private String id
    private String serverUrl
    private String credentialsId

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
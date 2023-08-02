package config

class JfrogBase {
    private String id
    private String serverUrl
    private String credentialsId
    private String reportsStorePath

    // constructor
    JfrogBase(String id, String serverUrl, String credentialsId, String reportsStorePath) {
        echo 'im here at jfrogbase constructor'
        echo id
        echo serverUrl
        echo credentialsId
        echo reportsStorePath
        
        this.id = id
        this.serverUrl = serverUrl
        this.credentialsId = credentialsId
        this.reportsStorePath = reportsStorePath
    }

    boolean CreateServer() {
        // create artifactory server
        try {
            rtServer (
                id: this.id,
                url: this.serverUrl,
                credentialsId: this.credentialsId
                timeout: 10
            )
        } catch (Exception e) {
            echo "Failed to create Artifactory server at constructor ${e}"
            throw new Exception("Failed to create Artifactory server")
        }
    }

    boolean UploadReports(String fromDir, String[] filenames) {
        boolean allUploaded = true
        // upload reports to artifactory
        for (filename in filenames) {
            def spec = """{
                "files": [
                    {
                        "pattern": "${fromDir}/${filename}",
                        "target": "${reportsStorePath}/"
                    }
                ]
            }"""

            // upload file, throw exception if failed
            try {
                rtUpload (
                    serverId: this.id,
                    spec: spec
                )
            } catch (Exception e) {
                echo "Failed to upload ${filename} to ${reportsStorePath}"
                allUploaded = false
                throw new Exception("Failed to upload ${filename} to ${reportsStorePath}")
            }
        }

        return allUploaded
    }
}

// initiate the class and return instance
public JfrogBase GetInstance(String id, String serverUrl, String credentialsId, String reportsStorePath) {
    echo 'im here at jfrog.gvy getinstance'
    return new JfrogBase(id, serverUrl, credentialsId, reportsStorePath)
}

return this
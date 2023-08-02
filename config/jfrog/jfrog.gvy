class JfrogBase {
    private String id
    private String serverUrl
    private String credentialsId
    private String reportsStorePath
    private Artifacroty.server server

    JfrogBase(String id, String serverUrl, String credentialsId, String reportsStorePath) {
        this.id = id
        this.serverUrl = serverUrl
        this.credentialsId = credentialsId
        this.reportsStorePath = reportsStorePath

        // create artifactory server
        this.server = new Artifacroty.server(serverUrl, credentialsId)

        // if there was issue, throw exception
        if (server == null) {
            echo "Failed to create Artifactory server"
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
                server.upload spec
            } catch (Exception e) {
                echo "Failed to upload ${filename} to ${reportsStorePath}"
                allUploaded = false
                throw new Exception("Failed to upload ${filename} to ${reportsStorePath}")
            }
        }

        return allUploaded
    }
}
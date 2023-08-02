boolean CreateServer(String id, String serverUrl, String credentialsId, String reportsStorePath) {
    try {
        rtServer (
            id: id,
            url: serverUrl,
            credentialsId: credentialsId,
            timeout: 10,
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

return this
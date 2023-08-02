
import org.jenkinsci.plugins.workflow.steps.FlowInterruptedException
import groovy.transform.builder.Builder

@Builder
class JfrogBase {
    private String id
    private String serverUrl
    private String credentialsId
    private String reportsStorePath

    // constructor
    JfrogBase(String id, String serverUrl, String credentialsId, String reportsStorePath) {
        this.id = id
        this.serverUrl = serverUrl
        this.credentialsId = credentialsId
        this.reportsStorePath = reportsStorePath

        // CreateServer()
    }

//     // boolean CreateServer() {
//     //     // create artifactory server
//     //     try {
//     //         rtServer (
//     //             id: this.id,
//     //             url: this.serverUrl,
//     //             credentialsId: this.credentialsId,
//     //             timeout: 10
//     //         )
//     //     } catch (Exception e) {
//     //         echo "Failed to create Artifactory server at constructor ${e}"
//     //         throw new Exception("Failed to create Artifactory server")
//     //     }
//     // }

//     // boolean UploadReports(String fromDir, String[] filenames) {
//     //     boolean allUploaded = true
//     //     // upload reports to artifactory
//     //     for (filename in filenames) {
//     //         def spec = """{
//     //             "files": [
//     //                 {
//     //                     "pattern": "${fromDir}/${filename}",
//     //                     "target": "${this.reportsStorePath}/"
//     //                 }
//     //             ]
//     //         }"""

//     //         // upload file, throw exception if failed
//     //         try {
//     //             rtUpload (
//     //                 serverId: this.id,
//     //                 spec: spec
//     //             )
//     //         } catch (Exception e) {
//     //             echo "Failed to upload ${filename} to ${this.reportsStorePath}"
//     //             allUploaded = false
//     //             throw new Exception("Failed to upload ${filename} to ${this.reportsStorePath}")
//     //         }
//     //     }

//     //     return allUploaded
//     // }
}

// public JfrogBase _instance
private boolean instanceCreated = false

// initiate the class and return instance
public void init(String id, String serverUrl, String credentialsId, String reportsStorePath) {
    echo "im here at jfrog.gvy getinstance"
    if (!instanceCreated) {
        def _instance = new JfrogBase(id, serverUrl, credentialsId, reportsStorePath)
        return true
    } else {
        echo 'instance already exists'
        return false
    }
}

public void uploadReports(String fromDir, String[] filenames) {
    echo 'im here at jfrog.gvy uploadreports'
    _instance.UploadReports(fromDir, filenames)
}

return this
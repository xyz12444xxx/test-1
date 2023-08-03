
import org.jfrog.*
import org.jfrog.hudson.*
import org.jfrog.hudson.util.Credentials;
// _instance = null

// // initiate the class and return instance
// public void init(String id, String serverUrl, String repo, String credentialsId, String reportsStorePath) {
//     if (!_instance) {
//         _instance = new JfrogBase(id, serverUrl, repo, credentialsId, reportsStorePath)
//     } else {
//         echo 'instance already exists'
//     }
// }

// public void uploadReports(String fromDir, String[] filenames) {
//     echo 'im here at jfrog.gvy uploadreports'
//     _instance.UploadReports(fromDir, filenames)
// }

class JfrogBase {
    private String id
    private String serverUrl
    private String repo
    private String credentialsId
    private String reportsStorePath
    private ArtifactoryServer server

    // constructor
    JfrogBase(String id, String serverUrl, String repo, String credentialsId, String reportsStorePath) {
        this.id = id
        this.serverUrl = serverUrl
        this.repo = repo
        this.credentialsId = credentialsId
        this.reportsStorePath = reportsStorePath
        this.server = new createServer()
    }

    private ArtifactoryServer createServer() {
        // create artifactory server
        try {
            return ArtifactoryServer(
                this.id,
                this.serverUrl,
                this.credentialsId,                
                new Credentials("", ""),
                10
            )
        } catch (Exception e) {
            throw new Exception("Failed to create Artifactory server")
        }

        return null
    }

    boolean UploadReports(String fromDir, String[] filenames) {
        boolean allUploaded = true
        // upload reports to artifactory
        // for (filename in filenames) {
        //     // def spec = """{
        //     //     "files": [
        //     //         {
        //     //             "pattern": "${fromDir}/${filename}",
        //     //             "target": "${this.repo}/${this.reportsStorePath}/"
        //     //         }
        //     //     ]
        //     // }"""

        //     // upload file, throw exception if failed
        //     try {
        //         // rtUpload (
        //         //     serverId: this.id,
        //         //     spec: spec
        //         // )

                ArtifactoryServer.upload (
                    serverId: 'artifactory-1',
                    spec: """{
                            "files": [
                                {
                                    "pattern": "target/*.log",
                                    "target": "demo-work/logs/"
                                }
                            ]
                        }"""
                )
        //     } catch (Exception e) {
        //         allUploaded = false
        //         throw new Exception("Failed to upload ${filename} to ${this.reportsStorePath}")
        //     }
        // }

        return allUploaded
    }
}

// return this
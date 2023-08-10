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
        if (!copyAndZipFiles(filenames, fromDir, "reports.gz")) {
            echo "Failed to zip files"
            return
        }

        // def result = sh(script: "curl -XPUT -k -T ${fromDir}/${filename} ${this.serverUrl}/${this.repo}/${this.reportsStorePath}/${filename} -u " + '$USERNAME:$PASSWORD', returnStdout: true).trim()
        // pass the zipped file and show upload speed progress bar
        def result = sh(script: "curl -XPUT --progress-bar -k -T reports.gz ${this.serverUrl}/${this.repo}/${this.reportsStorePath}/reports.gz -u " + '$USERNAME:$PASSWORD', returnStdout: true).trim()
        echo "${result}"
    }
}

private boolean copyAndZipFiles(String[] filenames, String fromDir, String zipFilename) {
    // copy all the files to temporary folder
    // then zip the folder and name it
    // then delete the temporary folder

    // create a temporary folder
    try {
        def tempDir = sh(script: "mktemp -d", returnStdout: true).trim()
        echo "tempDir: ${tempDir}"

        // copy files to the temporary folder
        for (String filename : filenames) {
            sh "cp ${fromDir}/${filename} ${tempDir}"
            sh "ls -l ${tempDir}"
        }

        // zip the temporary folder
        sh "tar -czf ${zipFilename} ${tempDir}"
        sh "ls -l"

        // delete the temporary folder
        sh "rm -rf ${tempDir}"
    } catch (Exception e) {
        echo "Failed to copy and zip files-echo ${e}"
        return false
    }

    return true
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
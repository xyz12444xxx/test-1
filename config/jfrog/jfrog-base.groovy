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
        String zipFilename = "reports1"
        if (!copyAndZipFiles(filenames, fromDir, zipFilename)) {
            echo "Failed to zip files"
            return
        }

        // def result = sh(script: "curl -XPUT -k -T ${fromDir}/${filename} ${this.serverUrl}/${this.repo}/${this.reportsStorePath}/${filename} -u " + '$USERNAME:$PASSWORD', returnStdout: true).trim()
        // pass the zipped file and show upload speed progress bar
        def result = sh(script: "curl -XPUT --progress-bar -k -T ${zipFilename}.zip ${this.serverUrl}/${this.repo}/${this.reportsStorePath}/${zipFilename}.zip -u " + '$USERNAME:$PASSWORD', returnStdout: true).trim()
        echo "${result}"
    }
}

private boolean copyAndZipFiles(String[] filenames, String fromDir, String zipFilename) {
    try {
        // create a folder
        // copy files to the folder
        // zip the folder

        sh "mkdir -p ${zipFilename}_temp"
        for (String filename : filenames) {
            sh "cp ${fromDir}/${filename} ${zipFilename}_temp"
        }
        
        sh "tar -czvf ${zipFilename}.zip ${zipFilename}_temp"
        sh "ls -l"
        sh "ls -l ${zipFilename}_temp"
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
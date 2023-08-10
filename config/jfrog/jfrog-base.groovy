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
        // pwd and show files
        // sh "pwd"
        // sh "ls -l"
        // sh "ls -l ${fromDir}"
        def filepaths = []
        for (String filename : filenames) {
            // sh ls to see filenames with pattern, then append to filepaths
            // filepaths.add(sh(script: "ls ${fromDir}/${filename}", returnStdout: true).trim())
            // seperate filesnames in a seperate index
            // but a filename can be a pattern, so sh ls will give a list of files, but not in list format
            // so we need to split the string into a list
            String files = sh(script: "ls ${fromDir}/${filename}", returnStdout: true).trim()
            echo "${files}"
            // split the string into a list
            String[] filesList = files.split('\n')
            for (String file : filesList) {
                filepaths.add(file)
            }
            echo "${filepaths}"
        }
        if (!copyAndZipFiles(filepaths, "temp", "reports.zip")) {
            echo "Failed to zip files"
            return
        }

        // def result = sh(script: "curl -XPUT -k -T ${fromDir}/${filename} ${this.serverUrl}/${this.repo}/${this.reportsStorePath}/${filename} -u " + '$USERNAME:$PASSWORD', returnStdout: true).trim()
        // pass the zipped file and show upload speed progress bar
        def result = sh(script: "curl -XPUT --progress-bar -k -T temp/reports.zip ${this.serverUrl}/${this.repo}/${this.reportsStorePath}/reports.zip -u " + '$USERNAME:$PASSWORD', returnStdout: true).trim()
        echo "${result}"
    }
}

private boolean copyAndZipFiles(def filepaths, String toDir, String zipFilename) {
    // copy files to a directory
    for (String filepath : filepaths) {
        try {
            sh "cp ${filepath} ${toDir}"
        } catch (Exception e) {
            echo "Failed to copy file ${filepath} to ${toDir}"
            return false
        }
    }
    // tar files
    try {
        sh "tar -czf ${zipFilename} ${toDir}"
    } catch (Exception e) {
        echo "Failed to zip files in ${toDir}"
        return false
    }
    // check if the zip file exists
    return fileExists(zipFilename)
}

private boolean fileExists(String filename) {
    return fileExists(filename, false)
}

private boolean fileExists(String filename, boolean show) {
    if (show) {
        sh "ls -l ${filename}"
    }
    def result = sh(script: "ls -l ${filename}", returnStdout: true).trim()
    return result != null && result != ''
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
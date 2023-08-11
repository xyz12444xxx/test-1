import groovy.time.*

instantiated = false
private ArtifactoryBase _instance = null

public boolean init(String serverUrl, String repo, String credentialsId) {
    if (!instantiated) {
        try {
            _instance = new ArtifactoryBase(serverUrl, repo, credentialsId)
            node {
                withCredentials([usernamePassword(credentialsId: credentialsId, passwordVariable: 'JFROG_PASSWORD', usernameVariable: 'JFROG_USERNAME')]) {
                    // curl to check if server is up in a shell script
                    def server = sh(script: "curl -k ${serverUrl}/api/system/ping -u " + '$JFROG_USERNAME:$JFROG_PASSWORD', returnStdout: true).trim()
                    if (server != 'OK') {
                        echo "Server ${serverUrl} is not up"
                    }
                }
            }
        } catch(Exception e) {
            echo "error: ${e}"
            return false
        }
    } else {
        echo 'instance already exists'
        return false
    }

    instantiated = true
    return true
}

public boolean downloadArtifacts(String pr, String fileToDownload, String targetDir) {
    if (!_instance) {
        echo 'Artifactory instance not initialized'
        return false
    }

    Date timeStart = new Date()
    try {
        withCredentials([usernamePassword(credentialsId: _instance.credentialsId, passwordVariable: 'JFROG_PASSWORD', usernameVariable: 'JFROG_USERNAME')]) {
            String url = "${_instance.serverUrl}/${_instance.repo}/pr_${pr}/${file}"
            def result = sh(script: "curl -XGET -k -o ${targetDir} ${url} -u " + '$JFROG_USERNAME:$JFROG_PASSWORD', returnStdout: true).trim()
            echo "${result}"
            def errors = readJSON(text:result).errors
            if (errors && errors.size() > 0) {
                throw new Exception("downloading ${file} failed, status: ${errors[0].status}| message: ${errors[0].message}")
            }
            
            // tar unzip the file
            sh "tar -xzvf ${targetDir}.zip"
        }
    } catch (Exception e) {
        echo "downloading failed, error: ${e}"
        return false
    } finally {
        echo "time duration of download: ${getDuration(timeStart)}"
    }

    return true
}

public boolean uploadArtifacts(String pr, String fromDir, String[] filenames, String targetDir, String targetFile) {
    if (!_instance) {
        echo 'Artifactory instance not initialized'
        return false
    }

    Date timeStart = new Date()
    try {
        withCredentials([usernamePassword(credentialsId: _instance.credentialsId, passwordVariable: 'JFROG_PASSWORD', usernameVariable: 'JFROG_USERNAME')]) {
            String url = "${_instance.serverUrl}/${_instance.repo}/pr_${pr}/${targetDir}/${targetFile}.zip"

            if (!copyAndZip(fromDir, filenames, targetFile)) {
                echo "Failed to zip files"
                return false
            }

            def result = sh(script: "curl -XPUT -k -T ${targetFile} ${url} -u " + '$JFROG_USERNAME:$JFROG_PASSWORD', returnStdout: true).trim()
            echo "${result}"
            def errors = readJSON(text:result).errors
            if (errors && errors.size() > 0) {
                throw new Exception("uploading failed, status: ${errors[0].status}| message: ${errors[0].message}")
            }
        }
    } catch (Exception e) {
        echo "uploading failed, error: ${e}"
        return false
    } finally {
        echo "time duration of upload: ${getDuration(timeStart)}"
    }

    return true
}

private TimeDuration getDuration(Date start, Date end = new Date()) {
    return TimeCategory.minus(end, start)
}

private boolean copyAndZip(String fromDir, String[] filenames, String zipFilename) {
    try {
        String tempFile = "temp_upload_artifacts"
        sh "mkdir -p ${tempFile}"
        for (String filename : filenames) {
            sh "cp ${fromDir}/${filename} ${tempFile}"
        }
        
        sh "tar -czvf ${zipFilename} ${tempFile}"
        sh "ls -l"
        sh "ls -l ${tempFile}"
    } catch (Exception e) {
        echo "Failed to copy and zip files-echo ${e}"
        return false
    }

    return true
}

class ArtifactoryBase {
    public String serverUrl
    public String credentialsId

    public String repo

    // constructor
    ArtifactoryBase(String serverUrl, String repo, String credentialsId) {
        this.serverUrl = serverUrl
        this.repo = repo
        this.credentialsId = credentialsId
    }
}

return this
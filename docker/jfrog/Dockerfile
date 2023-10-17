# Use a base image (e.g., Ubuntu, CentOS, or another preferred base image)
FROM ubuntu:20.04

# Set environment variables (modify them as needed)
ENV ARTIFACTORY_VERSION=7.9.2
ENV ARTIFACTORY_HOME=/opt/artifactory

# Install required packages and dependencies
RUN apt-get update && \
    apt-get install -y openjdk-11-jre wget tar && \
    rm -rf /var/lib/apt/lists/*

# Define the correct URL for the JFrog Artifactory tar.gz download
ENV ARTIFACTORY_URL=https://releases.jfrog.io/artifactory/bintray-artifactory/org/artifactory/oss/jfrog-artifactory-oss/7.9.2/jfrog-artifactory-oss-7.9.2-linux.tar.gz

# Download and extract JFrog Artifactory
RUN wget ${ARTIFACTORY_URL} -O /tmp/artifactory.tar.gz && \
    tar xzf /tmp/artifactory.tar.gz -C /tmp && \
    mv /tmp/artifactory-oss-${ARTIFACTORY_VERSION} ${ARTIFACTORY_HOME} && \
    rm /tmp/artifactory.tar.gz

# Create a user for Artifactory and set permissions
RUN useradd -r -d ${ARTIFACTORY_HOME} -s /sbin/nologin artifactory && \
    chown -R artifactory:artifactory ${ARTIFACTORY_HOME}

# Expose Artifactory ports (you might need to expose additional ports as needed)
EXPOSE 8081

# Start Artifactory
USER artifactory
WORKDIR ${ARTIFACTORY_HOME}
CMD ["./app/bin/artifactory.sh"]
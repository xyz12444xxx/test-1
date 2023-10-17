# Use the official Jenkins LTS (Long Term Support) image as the base image
FROM jenkins/jenkins:lts

# Switch to the root user to install Python
USER root

# Update the package list and install Python and pip
RUN apt-get update && \
    apt-get install -y python3 python3-pip

# Switch back to the Jenkins user
USER jenkins

# You can add additional instructions here if needed

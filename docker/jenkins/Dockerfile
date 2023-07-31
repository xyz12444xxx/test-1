FROM jenkins/jenkins:lts
USER root
# install python
RUN apt-get update && apt-get install -y python3-pip

# set username and password
ENV JENKINS_USER admin
ENV JENKINS_PASS admin
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false

# install plugins
COPY plugins.txt /usr/share/jenkins/plugins.txt
RUN jenkins-plugin-cli -f /usr/share/jenkins/plugins.txt

USER jenkins
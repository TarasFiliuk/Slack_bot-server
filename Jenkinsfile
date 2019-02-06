pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr:'5'))
    }
    triggers {
        pollSCM('H */1 * * *')
    }
    tools {
        jdk 'Java 1.8 Oracle'
        maven 'Maven 3.3.9'
    }
    stages {
        stage('build') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('sonar-analyze') {
            when {
                branch 'dev'
            }
            steps {
                script {
                    def scannerHome = tool 'SonarQube Scanner';
                    withSonarQubeEnv('Forcelate SonarQube') {
                        sh "${scannerHome}/bin/sonar-scanner";
                    }
                }
            }
        }
        stage('docker :dev') {
            when {
                branch 'dev'
            }
            steps {
                sh './docker-push.sh dev $DOCKERHUB_USERNAME $DOCKERHUB_PASSWORD'
            }
        }
        stage('docker :latest') {
            when {
                branch 'master'
            }
            steps {
                sh './docker-push.sh latest $DOCKERHUB_USERNAME $DOCKERHUB_PASSWORD'
            }
        }
    }
    post {
        always {
            script {
                // https://issues.jenkins-ci.org/browse/JENKINS-46325
                // https://jenkins.io/doc/book/pipeline/jenkinsfile/
                // Workaround: ['SUCCESS'] isEqualTo [null]
                if (currentBuild.result == null) {
                    currentBuild.result = 'SUCCESS'
                }
            }
            notifications()
        }
    }
}

def notifications() {
    emailext (
        to: "$INFRASTRUCTURE_DEVELOPERS",
        subject: '${DEFAULT_SUBJECT}',
        body: '''${SCRIPT, template="pipeline-changes.template"}'''
    )
}
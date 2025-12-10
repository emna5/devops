pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {

        stage('GIT') {
            steps {
                git branch: 'master',
                    url: 'https://github.com/emna5/devops.git'
            }
        }

        stage('Compile Stage') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                // Use the SonarQube server you configured in Jenkins
                withSonarQubeEnv('MySonarServer') {
                    // Run the Maven command to analyze the code and send results to SonarQube
                    sh 'mvn clean verify sonar:sonar'
                }
            }
        }
    }
}

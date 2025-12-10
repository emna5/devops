pipeline {
    agent any

    triggers {
        cron('H/5 * * * *')
    }

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    options {
        timeout(time: 1, unit: 'HOURS')
    }

    environment {
        APP_ENV = "DEV"
    }

    stages {
        stage('Checkout') {
            steps {
                echo "===== Checking out code from GitHub ====="
                git credentialsId: 'gitToken', branch: 'master',
                    url: 'https://github.com/emna5/devops.git'
            }
        }

        stage('Build') {
            steps {
                echo "===== Building Spring Boot application ====="
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('SonarQube Analysis') {
    steps {
        echo "===== Running SonarQube analysis ====="
        withSonarQubeEnv('MySonarServer') { // this must match the SonarQube server name in Jenkins
            withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                sh 'mvn sonar:sonar -Dsonar.login=$SONAR_TOKEN -Dmaven.test.skip=true'
            }
        }
    }
}


        stage('Archive Artifact') {
            steps {
                echo "===== Archiving JAR file ====="
                archiveArtifacts artifacts: '*/target/*.jar', fingerprint: true
            }
        }

        stage('Create Docker Image') {
            steps {
                echo "===== Creating Docker image ====="
                sh 'docker build -t emnahmani/alpine:1.0.0 .'
            }
        }

        stage('Push Docker Image') {
            steps {
                echo "===== Pushing Docker image to DockerHub ====="
                script {
                    withCredentials([string(credentialsId: 'dockerhub', variable: 'dockerhubpwd')]) {
                        sh 'echo "$dockerhubpwd" | docker login -u emnahmani --password-stdin'
                    }
                    sh 'docker push emnahmani/alpine:1.0.0'
                }
            }
        }
    }

    post {
        always {
            echo "===== Pipeline executed ====="
        }
        success {
            echo "===== Pipeline executed successfully ====="
        }
        failure {
            echo "===== Pipeline execution failed ====="
        }
    }
}

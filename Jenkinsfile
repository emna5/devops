pipeline {
    agent any

    // Trigger the pipeline every 5 minutes
    triggers {
        cron('H/5 * * * *')
    }

    // Define tools and environment
    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    options {
        timeout(time: 1, unit: 'HOURS') // Set timeout limit
    }

    environment {
        APP_ENV = "DEV" // Define app environment
    }

    stages {
        // Stage to checkout code from GitHub
        stage('Checkout') {
            steps {
                echo "===== Checking out code from GitHub ====="
                git credentialsId: 'gitToken', branch: 'master',
                    url: 'https://github.com/emna5/devops.git'
            }
        }

        // Stage to build the Spring Boot application
        stage('Build') {
            steps {
                echo "===== Building Spring Boot application ====="
                sh 'mvn clean package -DskipTests'
            }
        }

        // Stage to run SonarQube analysis
        stage('SonarQube Analysis') {
            steps {
                echo "===== Running SonarQube analysis ====="
                withSonarQubeEnv('MySonarServer') {
                    sh 'mvn clean verify sonar:sonar'
                }
            }
        }

        // Stage to archive the JAR artifact
        stage('Archive Artifact') {
            steps {
                echo "===== Archiving JAR file ====="
                archiveArtifacts artifacts: '*/target/*.jar', fingerprint: true
            }
        }

        // Stage to create Docker image from the built JAR file
        stage('Create Docker Image') {
            steps {
                echo "===== Creating Docker image ====="
                sh 'docker build -t emnahmani/alpine:1.0.0 .'
            }
        }

        // Stage to push Docker image to DockerHub
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

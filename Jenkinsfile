pipeline {
    agent any

    triggers {
        cron('H/5 * * * *') // Build every 5 minutes
    }

    tools {
        jdk 'JAVA_HOME'   // Make sure this matches your Jenkins JDK name
        maven 'M2_HOME'   // Make sure this matches your Jenkins Maven name
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
                echo "===== Building Spring Boot application (with tests) ====="
                sh 'mvn clean install'  // Tests will run here
            }
        }

        stage('Test') {
            steps {
                echo "===== Running unit tests ====="
                sh 'mvn test'  // Optional, but can generate additional test reports
            }
        }

        stage('Package') {
            steps {
                echo "===== Packaging Spring Boot application ====="
                sh 'mvn package'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo "===== Running SonarQube analysis ====="
                withSonarQubeEnv('MySonarServer') {
                    withCredentials([string(credentialsId: 'sonartoken1', variable: 'SONAR_TOKEN')]) {
                        sh 'mvn clean verify sonar:sonar -Dsonar.login=$SONAR_TOKEN -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                    }
                }
            }
        }


        stage('Archive Artifact') {
            steps {
                echo "===== Archiving JAR file ====="
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Create Docker Image') {
            steps {
                echo "===== Creating Docker image ====="
                sh 'docker build -t emnahmani/alpine:1.0.0 -t emnahmani/alpine:latest .'
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
                    sh 'docker push emnahmani/alpine:latest'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                echo "===== Checking SonarQube Quality Gate ====="
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
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

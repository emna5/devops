pipeline {
    agent any

    triggers {
        cron('H/5 * * * *') // Build every 5 minutes
    }

    tools {
        jdk 'JAVA_HOME'   // Jenkins JDK
        maven 'M2_HOME'   // Jenkins Maven
    }

    environment {
        APP_ENV = "DEV"
    }

    options {
        timeout(time: 1, unit: 'HOURS')
    }

    stages {

        stage('Checkout') {
            steps {
                echo "===== Checking out code from GitHub ====="
                git credentialsId: 'gitToken', branch: 'master',
                    url: 'https://github.com/emna5/devops.git'
            }
        }

        stage('Start MySQL Test DB') {
            steps {
                echo "===== Starting MySQL test database ====="
                sh '''
                    docker rm -f studentdb-test || true   # remove if exists
                    docker run --name studentdb-test -e MYSQL_ROOT_PASSWORD= -e MYSQL_DATABASE=studentdb_test -p 3306:3306 -d mysql:8
                    sleep 15  # wait for DB to initialize
                '''
            }
        }

        stage('Build, Test & SonarQube Analysis') {
            steps {
                echo "===== Building, running tests, and sending SonarQube analysis ====="
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

        stage('Stop MySQL Test DB') {
            steps {
                echo "===== Stopping MySQL test database ====="
                sh 'docker rm -f studentdb-test'
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

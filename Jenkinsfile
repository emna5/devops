pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {

       stage('GIT') {
    steps {
        git credentialsId: 'gitToken', branch: 'master',
            url: 'https://github.com/emna5/devops.git'
    }
}

        sstage('Compile Stage') {
    steps {
        sh 'mvn clean compile -DskipTests'
    }
}


       stage('SonarQube Analysis') {
    environment {
        SONAR_TOKEN = credentials('SonarQube Token')
    }
    steps {
        withSonarQubeEnv('MySonarServer') {
            sh 'mvn sonar:sonar -Dsonar.login=$SONAR_TOKEN'
        }
    }
}

    }
}

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

        stage('Compile Stage') {
    steps {
        sh 'mvn clean compile -DskipTests'
    }
}


       stage('SonarQube Analysis') {
    steps {
        withSonarQubeEnv('MySonarServer') {
            sh 'mvn clean verify sonar:sonar'
        }
    }
}


    }
}

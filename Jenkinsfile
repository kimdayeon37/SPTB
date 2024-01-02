pipeline {
    agent any

    stages {
        stage('Make Jar') {
            steps {
                script {
                    dir('./backend') {
                        sh 'ls -la'
                        sh 'gradle wrapper --gradle-version 8.3'
                        sh './gradlew bootJar'
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Backend build successful!'
        }
        failure {
            echo 'Backend build failed!'
        }
    }
}

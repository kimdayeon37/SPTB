pipeline {
    agent any

    stages {
        stage('Make Jar') {
            steps {
                script {
                    dir('./backend') {
                        sh 'ls -a'
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

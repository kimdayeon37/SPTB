pipeline {
    agent any

    stages {
        stage('Make Jar') {
            steps {
                script {
                    dir('/home/angrymusic/.jenkins/workspace/sptb_master/backend') {
                        sh 'ls -la'
                        sh '/home/angrymusic/.jenkins/workspace/sptb_master/backend/gradlew bootJar'
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

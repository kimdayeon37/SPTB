pipeline {
    agent any

    stages {
        

        stage('Make Jar') {
            steps {
                script {
                    dir('./backend') {
                        sh './gradlew bootJar --offline'
                    }
                }
            }
        }

        // 다른 스테이지들을 여기에 추가할 수 있습니다.
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

pipeline {
    agent any

    stages {
        stage('Make Jar') {
            steps {
                script {
                    echo 'Current Directory: ' + pwd()
                    dir('./backend') {
                        sh 'ls -l' // 디렉토리 내의 파일 목록 출력
                        sh 'chmod +x gradlew'
                        sh './gradlew --version' // Gradle Wrapper 버전 확인
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

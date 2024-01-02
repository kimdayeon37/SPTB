pipeline {
    agent any

    stages {
        stage('Make Jar') {
            steps {
                script {
                    // 프로젝트 디렉토리로 이동
                    dir('./backend') {
                        // Gradle Wrapper를 초기화 (이미 초기화되어 있다면 필요하지 않음)
                        sh './gradlew wrapper --gradle-version 8.3'

                        // Gradle Wrapper를 사용하여 bootJar 실행
                        sh './gradlew bootJar'
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

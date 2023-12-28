pipeline {
    agent any

    stages {
        stage('Build Backend') {
            steps {
                script {
                    // /backend 폴더로 이동
                    dir('/backend') {
                        // Gradle 빌드 명령어 실행
                        sh './gradlew build'
                        // Gradle 빌드 출력 확인
                        echo 'Gradle build output:'
                        sh 'cat build/build.log'
                    }
                }
            }
        }

        // 다른 스테이지들을 여기에 추가할 수 있습니다.
    }

    post {
        success {
            // 빌드 성공 시 추가 작업 수행 가능
            echo 'Backend build successful!'
        }
        failure {
            // 빌드 실패 시 추가 작업 수행 가능
            echo 'Backend build failed!'
        }
    }
}

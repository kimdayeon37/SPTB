pipeline {
    agent any

    stages {
        stage('Change Permissions!') {
            steps {
                script {
                    // /backend 폴더로 이동
                    dir('./backend') {
                        // Jenkins 사용자로 소유자 변경
                        sh 'sudo chown -R stem:stem .'
                        // 적절한 권한 부여
                        sh 'sudo chmod -R u+rwX,g+rX,o+rX .'
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

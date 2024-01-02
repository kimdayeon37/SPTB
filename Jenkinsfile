pipeline {
    agent any

    stages {
        stage('Make Jar') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'angrymusic-sudo', passwordVariable: 'SUDO_PASSWORD', usernameVariable: 'angrymusic')]) {
                    script {
                        dir('./backend') {
                            sh 'whoami'
                            sh './gradlew bootJar'

                            // Assuming your jar file is generated in the 'build/libs' directory
                            def jarFileName = sh(script: "ls -t build/libs/*.jar | head -n 1 | awk -F'/' '{print \$NF}'", returnStdout: true).trim()
                            def jarFilePath = "build/libs/${jarFileName}"

                            // Print the generated jar file name for reference
                            echo "Generated JAR file: ${jarFileName}"

                            // Copy the jar file to init.d directory (modify the path accordingly)
                            sh "echo ${SUDO_PASSWORD} | sudo cp ${jarFilePath} /etc/init.d/sptb"

                            // Restart the service (modify the service name accordingly)
                            sh "echo ${SUDO_PASSWORD} | sudo service sptb restart"
                        }
                    }
                }   
            }
        }
    }

    post {
        success {
            echo 'Backend build and deployment successful!'
        }
        failure {
            echo 'Backend build failed!'
        }
    }
}

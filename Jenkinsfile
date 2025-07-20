pipeline{

    agent any

    stages{

        stage('Build Jar'){
            steps{
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build Image'){
            steps{
                bat 'docker build -t=mtaha7/bookstore-api-image:lts .'
            }
        }

        stage('Push Image'){
            environment{
                DOCKER_HUB = credentials('dockerhub-creds')
            }
            steps{
                bat 'docker login -u %DOCKER_HUB_USR% -p %DOCKER_HUB_PSW%'
                bat 'docker push mtaha7/bookstore-api-image:lts'
            }
        }

        stage('run-test'){
            steps{
               bat 'docker run -v ./test-output/ExtentReport:/home/api-docker/test-output/ExtentReport mtaha7/bookstore-api-image:lts'
               bat 'docker run mtaha7/bookstore-api-image:lts'
            }
        }

    }

    post {
        always {
            bat 'docker logout'
            archiveArtifacts artifacts: 'test-output/ExtentReport/BSReport*.html', followSymlinks: false
        }
    }

}
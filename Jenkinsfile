pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh '''#Cambiamos al directorio del proyecto
                    cd /home/peluca/proyecto/proyecto-final
                    git pull origin master
                    #Generamos dockerfile
                    docker build -t frontend:${BUILD_NUMBER} -f nginx/Dockerfile .
                    '''
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}

pipeline {
    agent any

    stages {
        stage('Code Review') {
            steps {
                echo 'Code review....'
                input 'Continue with the pipeline for the BUILD:\${BUILD_NUMBER} ?'
            }
        }
        stage('Build Docker images') {
            steps {
                sh '''#Cambiamos al directorio del proyecto
                    cd /home/peluca/proyecto/proyecto-final
                    git pull origin master
                    #Generamos dockerfile
                    docker build -t frontend:${BUILD_NUMBER} -f nginx/Dockerfile .
                    docker build -t backend:${BUILD_NUMBER} -f springBoot/Dockerfile .
                    docker build -t database:${BUILD_NUMBER} -f mariaDb/Dockerfile .
                    '''
            }
        }
        stage('Run new docker images') {
            steps {
                sh '''#Paramos los docker corriendo actualmente en el servidor
                    docker stop $(docker ps -a -q)
                    #Borramos dockers antiguos
                    docker rm $(docker ps -a -q)
                    #Iniciamos los docker con las nuevas imágenes
                    docker run --name front_${BUILD_NUMBER} -p 8081:80 -d frontend:${BUILD_NUMBER}
                   '''
            }
        }
        stage('Test') {
            steps {
                echo 'Testing....'
            }
        }
        stage('Deploy') {
            steps {
                input 'Continue with the deploy of the BUILD:\${BUILD_NUMBER} ?'
            }
        }
        stage('Rollback') {
            steps {
                echo 'Performing Rollback....'
            }
        }
    }
}

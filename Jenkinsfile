pipeline {
    agent any

    stages {
        stage('Code Review') {
            steps {
                echo 'Code review....'
                input 'Continue with the pipeline ?'
                emailext body: "El code review de la aplicacion fue satisfactorio. Version build ${BUILD_NUMBER}", subject: "Code Review status version ${BUILD_NUMBER}", to: 'michel.rivas@estudiantes.utec.edu.uy'
            }
        }
        stage('Build Docker images') {
            steps {
                sh '''#Cambiamos al directorio del proyecto
                    cd /home/utec/proyecto/proyecto-final
                    git pull origin master
                    #Generamos dockerfile
                    docker build -t frontend:${BUILD_NUMBER} -f node/Dockerfile .
                    docker build -t backend:${BUILD_NUMBER} -f springBoot/Dockerfile .
                    docker build -t database:${BUILD_NUMBER} -f mariaDb/Dockerfile .
                    '''
            }
        }
        stage('Test') {
            steps {
                script {
                    try {
                        echo 'Testing...'
                        sh 'exit 1'
                    }
                    catch (exc) {
                        echo 'Testing failed!'
                        currentBuild.result = 'UNSTABLE'
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                emailext body: "El testing presenta status ${currentBuild.result}. Version build ${BUILD_NUMBER}", subject: "Testing ${currentBuild.result}- Build ${BUILD_NUMBER}", to: 'michel.rivas@estudiantes.utec.edu.uy'
                input 'Continue with the deploy?'
                sh '''#Paramos los docker corriendo actualmente en el servidor
                    docker stop $(docker ps -a -q)
                    #Borramos dockers antiguos
                    docker rm $(docker ps -a -q)
                    #Iniciamos los docker con las nuevas imágenes
                    docker run --name front_${BUILD_NUMBER} -p 8081:8080 -d frontend:${BUILD_NUMBER}
                   '''
		emailext body: "Se realizo el deploy de la aplicacion version ${BUILD_NUMBER}", subject: "Deploy version ${BUILD_NUMBER}", to: 'michel.rivas@estudiantes.utec.edu.uy'
            }
        }
    }
    post {
        failure {
            echo 'Rollback changes...'
            echo 'Enviando correo'
        }
        success {
            echo "Version actualizada BUILD:${BUILD_NUMBER}"
            echo 'Enviando correo'
        }
        unstable {
            echo "Version actualizada BUILD:${BUILD_NUMBER} INESTABLE"
            echo 'Enviando correo'
        }
    }
}

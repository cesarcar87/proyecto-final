pipeline {
    agent any

    stages {
        stage('Code Review') {
            steps {
                echo 'Code review....'
                emailext body: "El code review de la aplicacion fue satisfactorio. Version build ${BUILD_NUMBER}", subject: "Code Review status version ${BUILD_NUMBER}", to: 'michel.rivas@estudiantes.utec.edu.uy'
                input 'Continue with the pipeline ?'
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
                        currentBuild.currentResult = 'UNSTABLE'
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                emailext body: "El testing presenta status ${currentBuild.currentResult}. Version build ${BUILD_NUMBER}", subject: "Testing ${currentBuild.currentResult}- Build ${BUILD_NUMBER}", to: 'michel.rivas@estudiantes.utec.edu.uy'
                input 'Continue with the deploy?'
                sh '''#Paramos los docker corriendo actualmente en el servidor
                    docker stop $(docker ps -a -q)
                    #Borramos dockers antiguos
                    docker rm $(docker ps -a -q)
                    #Iniciamos los docker con las nuevas imágenes
                    docker run --name front_${BUILD_NUMBER} -p 8081:8080 -d frontend:${BUILD_NUMBER}
                   '''
            }
        }
    }
    post {
        failure {
            echo 'Rollback changes...'
            emailext body: "Fallo en build de la version ${BUILD_NUMBER}", subject: "Fallo en deploy version ${BUILD_NUMBER}", to: 'michel.rivas@estudiantes.utec.edu.uy'
        }
        success {
            echo "Version actualizada BUILD:${BUILD_NUMBER}"
            emailext body: "Se realizo el deploy de la aplicacion version ${BUILD_NUMBER}", subject: "Deploy version ${BUILD_NUMBER}", to: 'michel.rivas@estudiantes.utec.edu.uy'
        }
        unstable {
            emailext body: "Se realizo el deploy de la aplicacion version ${BUILD_NUMBER} ${currentBuild.currentResult}", subject: "Deploy ${currentBuild.currentResult} version ${BUILD_NUMBER}", to: 'michel.rivas@estudiantes.utec.edu.uy'
            echo 'Enviando correo'
        }
    }
}

pipeline {

    agent any

    tools {
        jdk 'JDK21'
    }

    environment {
        APP_NAME = 'spring-boot-api-rest-persona'
        MAVEN_OPTS = '-Xmx1024m'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Clonando repositorio desde GitHub...'
                checkout scm
            }
        }

        stage('Validar Maven Wrapper') {
            steps {
                echo 'Validando Maven Wrapper...'
                sh 'chmod +x mvnw'
                sh './mvnw -version'
            }
        }

        stage('Limpiar Proyecto') {
            steps {
                echo 'Ejecutando clean...'
                sh './mvnw clean'
            }
        }

        stage('Compilar Aplicación') {
            steps {
                echo 'Compilando proyecto Spring Boot...'
                sh './mvnw compile'
            }
        }

        stage('Pruebas Unitarias') {
            steps {
                echo 'Ejecutando pruebas unitarias...'
                sh './mvnw test -DskipTests'
            }
        }

        stage('Pruebas de Integración') {
            steps {
                echo 'Ejecutando pruebas de integración...'
                sh './mvnw verify -DskipTests'
            }
        }

        stage('Empaquetar JAR') {
            steps {
                echo 'Generando artefacto JAR...'
                sh './mvnw package -DskipTests'
            }
        }

        stage('Validar Artefacto') {
            steps {
                echo 'Mostrando archivos generados...'
                sh 'ls -lh target/'
            }
        }

    }

    post {

        success {
            echo 'Pipeline ejecutado correctamente.'
        }

        failure {
            echo 'El pipeline falló.'
        }

        always {
            echo 'Fin de ejecución Jenkins.'
        }
    }
}
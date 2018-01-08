/* Build jenkins-unit-testing */
pipeline {
    /* Selected agent */
    agent { label 'p4es-maven-slave' }

    /* Maven command */
    def mvncmd = 'mvn33'

    /* Pipeline stages */
    stages {
        /* Compile */
        stage('Compile'){
            steps {
                sh "${mvncmd} clean compile"
            }
        }

        /* Test library */
        stage('Test') {
            steps {
                sh "${mvncmd} test"
            }
        }

        /* Publish library */
        stage('Publish'){
            steps {
                sh "${mvncmd} install"
            }
        }
    }
}
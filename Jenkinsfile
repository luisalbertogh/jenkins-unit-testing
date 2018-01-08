/* Build jenkins-unit-testing */
pipeline {
    /* Selected agent */
    agent { label 'p4es-maven-slave' }

    /* Pipeline stages */
    stages {
        /* Compile */
        stage('Compile'){
            steps {
                sh 'mvn33 clean compile'
            }
        }

        /* Test library */
        stage('Test') {
            steps {
                sh 'mvn33 test'
            }
        }

        /* Publish library */
        stage('Publish'){
            steps {
                sh 'mvn33 install'
            }
        }
    }
}
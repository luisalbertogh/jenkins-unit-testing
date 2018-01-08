/* Build jenkins-unit-testing */
pipeline {
    /* Selected agent */
    agent { label 'p4es-maven-slave' }

    /* Params */
    parameters {
        string(name: 'MVN', defaultValue: 'mvn33', description: 'Maven executable')
    }

    /* Pipeline stages */
    stages {
        /* Compile */
        stage('Compile'){
            steps {
                sh "${params.MVN} clean compile"
            }
        }

        /* Test library */
        stage('Test') {
            steps {
                sh "${params.MVN} test"
            }
        }

        /* Publish library */
        stage('Publish'){
            steps {
                sh "${params.MVN} install"
            }
        }
    }
}
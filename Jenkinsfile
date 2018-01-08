/* Build jenkins-unit-testing */
pipeline {
    /* Selected agent */
    //agent { label 'p4es-maven-slave' }
    agent { label 'docker-slaves' }

    /* Params */
    parameters {
        string(name: 'MVN', defaultValue: 'mvn', description: 'Maven executable')
    }

    /* Pipeline stages */
    stages {
        /* Compile */
        stage('Build'){
            steps {
                sh "${params.MVN} clean compile"
            }
        }

        /* Test */
        stage('Test') {
            steps {
                sh "${params.MVN} test"
            }
        }

        /* Package and upload to binary repo */
        stage('Store'){
            steps {
                sh "${params.MVN} package"
            }
        }
    }
}
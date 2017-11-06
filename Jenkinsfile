/* Build jenkins-unit-testing */
node('docker-slaves') {
    /* Checkout library */
    stage('Checkout'){
        checkout scm
    }
    
    /* Compile library */
    stage('Compile'){
        sh "mvn clean compile"
    }
    
    /* Test library */
    stage('Test'){
        sh "mvn test"
    }
    
    /* Publish library */
    stage('Publish'){
        sh "mvn install"
    }
}
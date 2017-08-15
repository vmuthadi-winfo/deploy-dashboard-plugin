#!groovy​

node('master') {
    try {
        def mvn = docker.image("maven:3.5.0-jdk-7-alpine")
        mvn.inside() {
            def repository = checkout scm
            step([$class: 'StashNotifier'])

            stage('Build') {
                sh 'mvn package'
                archiveArtifacts artifacts: 'target/environment-dashboard.hpi'
            }
            if (env.BRANCH_NAME == 'master') {
                stage('Deploy to S3') {
                    withAWS() {
                        s3Upload(
                                file: 'target/environment-dashboard.hpi',
                                bucket: 'distributions.devops.namecheap.net',
                                path: "jenkins/plugins/environment-dashboard-${repository.GIT_COMMIT}-${env.BUILD_NUMBER}.hpi"
                        )
                    }
                }
            }
        }
        currentBuild.result = 'SUCCESS'
    } catch(e) {
        echo "Caught: ${e}"

        currentBuild.result = 'FAILED'
    } finally {
        step([$class: 'StashNotifier'])
    }
}
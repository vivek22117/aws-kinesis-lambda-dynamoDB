pipeline {
    agent {
        label 'dev'
    }

    options {
        preserveStashes(buildCount: 5)
        timeout(time: 20, unit: 'MINUTES')
        skipStagesAfterUnstable()
    }
    parameters {
        string(name: 'REGION', defaultValue: 'us-east-1', description: 'AWS region specified')
        booleanParam(name: 'DESTROY', defaultValue: 'true')
    }
    environment {
        PATH = "${PATH}:${getTerraformPath()}"
        EMAIL_TO = 'vivekmishra22117@gmail.com'
    }

    stages {
        stage('destroy') {
            steps {
                dir('aws-infra/lambda-fixed-resources/') {
                    script {
                        def isDestroy = ${params.DESTROY}
                        if(isDestroy){
                            sh "terraform destroy -auto-approve -force"
                            sh "echo 'Skipped'"
                            return
                        }
                    }
                }
            }
        }
        stage('build') {
            steps {
                dir('kinesis-stream-processing/') {
                    script {
                        def mvnHome = tool 'Maven'
                        sh "'${mvnHome}/bin/mvn' clean install -Dintegration-tests.skip=true"
                    }
                }
            }
        }
        stage('tf-init') {
            steps {
                dir('aws-infra/lambda-fixed-resources/') {
                    script {
                        sh "terraform --version"
                        sh "terraform init"
                        sh "whoami"
                    }
                }
            }
        }
        stage('tf-plan') {
            steps {
                dir('aws-infra/lambda-fixed-resources/') {
                    script {
                        sh "terraform plan -out rsvp-lambda-processor.tfplan; echo \$? > status"
                        def exitCode = readFile('status').trim()
                        echo "Terraform Plan Exit Code: ${exitCode}"
                        stash name: "rsvp-lambda-processor-plan", includes: "rsvp-lambda-processor.tfplan"
                    }
                }
            }
        }
        stage('tf-apply') {
            steps {
                dir('aws-infra/lambda-fixed-resources/') {
                    script {
                        def apply = false
                        try {
                            input message: 'confirm apply', ok: 'Apply config'
                            apply = true;
                        } catch (err) {
                            apply = false
                            sh "terraform destroy -auto-approve -force"
                        }
                        if (apply) {
                            unstash "rsvp-lambda-processor-plan"
                            sh "terraform apply -auto-approve rsvp-lambda-processor.tfplan"
                        }
                    }
                }
            }
        }
    }
     post {
       // Always runs. And it runs before any of the other post conditions.
       always {
         // Let's wipe out the workspace before we finish!
         deleteDir()
       }

       success {
        sendEmail('Successful')
       }

       failure {
        sendEmail('Failed')
       }
     }
}

def sendEmail(status) {
    mail(
            to: "$EMAIL_TO",
            subject: "Build $BUILD_NUMBER - " + status + " (${currentBuild.fullDisplayName})",
            body: "Changes:\n " + getChangeString() + "\n\n Check console output at: $BUILD_URL/console" + "\n")
}

def getTerraformPath() {
    def tfHome = tool name: "Terraform", type: "org.jenkinsci.plugins.terraform.TerraformInstallation"
    return tfHome
}
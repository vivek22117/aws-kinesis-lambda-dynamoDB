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
        choice(
            choices: ['create', 'destroy'],
            description: '',
            name: 'AWS_INFRA_ACTION')
    }
    environment {
        PATH = "${PATH}:${getTerraformPath()}"
        EMAIL_TO = 'vivekmishra22117@gmail.com'
    }

    stages {
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
                        sh "terraform plan -var 'environment=${NODE_NAME}' -out rsvp-lambda-processor.tfplan; echo \$? > status"
                        def exitCode = readFile('status').trim()
                        echo "Terraform Plan Exit Code: ${exitCode}"
                        stash name: "rsvp-lambda-processor-plan", includes: "rsvp-lambda-processor.tfplan"
                    }
                }
            }
        }
        stage('destroy') {
            when {
                expression {
                    "${params.AWS_INFRA_ACTION}" == "destroy"
                }
            }
            steps {
                dir('aws-infra/lambda-fixed-resources/') {
                    script {
                        input message: 'Destroy Plan?', ok: 'Destroy'
                        sh "echo destroying the AWS infra....."
                        sh "terraform destroy -auto-approve -force"
                    }
                }
            }
        }
        stage('tf-apply') {
            when {
                expression {
                    "${params.AWS_INFRA_ACTION}" == "create"
                }
            }
            steps {
                dir('aws-infra/lambda-fixed-resources/') {
                    script {
                        def apply = false
                        try {
                            input message: 'confirm apply', ok: 'Apply config'
                            apply = true;
                        } catch (err) {
                            apply = false
                            sh "echo skipping the AWS infra creation....."
                        }
                        if (apply) {
                            sh "echo creating AWS infra....."
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
            subject: "Build ${env.BUILD_NUMBER} - " + status + " (${currentBuild.fullDisplayName})",
            body: "Changes:\n " + getChangeString() + "\n\n Check console output at: ${env.BUILD_URL}/console" + "\n")
}

def getTerraformPath() {
    def tfHome = tool name: "Terraform", type: "org.jenkinsci.plugins.terraform.TerraformInstallation"
    return tfHome
}

// get change log to be send over the mail
@NonCPS
def getChangeString() {
    MAX_MSG_LEN = 100
    def changeString = ""

    echo "Gathering SCM changes"
    def changeLogSets = currentBuild.changeSets
    for (int i = 0; i < changeLogSets.size(); i++) {
        def entries = changeLogSets[i].items
        for (int j = 0; j < entries.length; j++) {
            def entry = entries[j]
            truncated_msg = entry.msg.take(MAX_MSG_LEN)
            changeString += " - ${truncated_msg} [${entry.author}]\n"
        }
    }

    if (!changeString) {
        changeString = " - No new changes"
    }
    return changeString
}
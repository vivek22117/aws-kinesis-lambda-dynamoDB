pipeline {
    agent any

    tools {
        "org.jenkinsci.plugins.terraform.TerraformInstallation" "Terraform"
    }

    options {
        preserveStashes(buildCount: 5)
        timeout(time: 20, unit: 'MINUTES')
        skipStagesAfterUnstable()
    }
    parameters {
        string(name: 'REGION', defaultValue: 'us-east-1', description: 'AWS region specified')
        string(name: 'WORKSPACE', defaultValue: 'development', description: 'workspace to use in Terraform')
        booleanParam(name: 'DESTROY', defaultValue: 'true')
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
}

def getTerraformPath() {
    def tfHome = tool name: "Terraform", type: "org.jenkinsci.plugins.terraform.TerraformInstallation"
    return tfHome
}
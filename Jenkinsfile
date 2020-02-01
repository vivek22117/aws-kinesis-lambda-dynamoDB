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
        TF_HOME = tool('Terraform')
        TF_IN_AUTOMATION = "true"
        PATH = "$TF_HOME:$PATH"
        AWS_METADATA_URL = "http://169.254.169.254:80/latest"
        AWS_METADATA_ENDPOINT = "http://169.254.169.254:80/latest"
        AWS_METADATA_TIMEOUT = "2s"
        EMAIL_TO = 'vivekmishra22117@gmail.com'
        POM_VERSION = readMavenPom().getVersion()
        BUILD_RELEASE_VERSION = readMavenPom().getVersion().replace("-SNAPSHOT", "")
        IS_SNAPSHOT = readMavenPom().getVersion().endsWith("-SNAPSHOT")
        GIT_TAG_COMMIT = sh(script: 'git describe --tags --always', returnStdout: true).trim()
    }

    stages {
        stage('build') {
            steps {
                dir('kinesis-stream-processing/') {
                    script {
                    echo 'Pulling...' + env.BRANCH_NAME
                    def mvnHome = tool 'Maven 3.3.9'
                        bat("${mvnHome}/bin/mvn" -Dintegration-tests.skip=true clean install)
                        def pom = readMavenPom file: 'pom.xml'
                        print pom.version
                        junit '**//*target/surefire-reports/TEST-*.xml'
                        archive 'target*//*.jar'
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
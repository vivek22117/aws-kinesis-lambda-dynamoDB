package com.dd.rsvp.processor.ci.builder

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

class RSVPLambdaProcessorBuilder {

    DslFactory dslFactory
    String jobName
    String description
    String displayName
    String githubUrl
    String branchesName
    String credentialId
    String environment
    String scriptPath

    Job build() {
        dslFactory.pipelineJob(jobName) {
            description(description)
            displayName(displayName)

            definition {
                cpsScm {
                    scm {
                        git {
                            branch(branchesName)
                            remote {
                                url(githubUrl)
                                credentials(credentialId)
                            }
                        }
                        scriptPath(scriptPath)
                        lightweight(true)
                    }
                }
            }
            parameters {
                stringParam('ENVIRONMENT', environment)
                nodeParam('Label') {
                    description('Select the node to execute the job!')
                    defaultNodes([environment])
                }
            }
        }
    }
}

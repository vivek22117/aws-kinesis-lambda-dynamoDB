package com.dd.rsvp.processor.ci.script

import com.dd.rsvp.processor.ci.builder.RSVPLambdaProcessorBuilder
import javaposse.jobdsl.dsl.JobParent

def factory = this as JobParent
def listOfEnvironment = ["dev", "qa", "prod"]
def component = "rsvp-lambda-processor-job"

def scriptLocation = "jenkins/"
def description = "Pipeline DSL to create build RSVP Lambda Processor App and Infra"
def displayName = "RSVP Lambda Processor Job"
def branchesName = "*/master"
def githubUrl = "https://github.com/vivek22117/aws-kinesis-lambda-dynamodDB.git"


new RSVPLambdaProcessorBuilder(
        dslFactory: factory,
        description: description,
        jobName: component + "-" + listOfEnvironment.get(0),
        displayName: displayName + " " + listOfEnvironment.get(0),
        branchesName: branchesName,
        githubUrl: githubUrl,
        credentialId: 'github',
        environment: listOfEnvironment.get(0),
        scriptPath: scriptLocation

).build()


new RSVPLambdaProcessorBuilder(
        dslFactory: factory,
        description: description,
        jobName: component + "-" + listOfEnvironment.get(1),
        displayName: displayName + " " + listOfEnvironment.get(1),
        branchesName: branchesName,
        githubUrl: githubUrl,
        credentialId: 'github',
        environment: listOfEnvironment.get(1),
        scriptPath: scriptLocation
).build()


new RSVPLambdaProcessorBuilder(
        dslFactory: factory,
        description: description,
        jobName: component + "-" + listOfEnvironment.get(2),
        displayName: displayName + " "+ listOfEnvironment.get(2),
        branchesName: branchesName,
        githubUrl: githubUrl,
        credentialId: 'github',
        environment: listOfEnvironment.get(2),
        scriptPath: scriptLocation
).build()

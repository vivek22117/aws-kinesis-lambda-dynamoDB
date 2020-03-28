package com.dd.rsvp.processor.ci.script

import com.dd.rsvp.processor.ci.builder.LambdaFixedResourceBuilder
import javaposse.jobdsl.dsl.JobParent

def factory = this as JobParent
def listOfEnvironment = ["dev", "qa", "prod"]
def component = "lambda-fixed-resources-job"

def scriptLocation = "jenkins/"
def description = "Pipeline DSL to create RSVP Lambda fixed resource, Kinesis & DynamoDB"
def displayName = "Lambda Fixed Resources Job"
def branchesName = "*/master"
def githubUrl = "https://github.com/vivek22117/aws-kinesis-lambda-dynamodDB.git"


new LambdaFixedResourceBuilder(
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


new LambdaFixedResourceBuilder(
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


new LambdaFixedResourceBuilder(
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

package ru.clevertec.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class GitTagVersionTask extends DefaultTask {

    private static final Logger logger = LoggerFactory.getLogger(GitTagVersionTask.class)

    @TaskAction
    void tagVersion() {
        def currentBranch = getCurrentGitBranch()
        logger.info("Current branch: {}", currentBranch)
        def lastTag = getLastGitTag()
        logger.info("Last tag: {}", lastTag)

        if (hasUncommittedChanges()) {
            println "Build number: ${project.version}.uncommited"
            return
        }

        def newVersion = calculateNewVersion(currentBranch, lastTag)
        logger.info("New version: {}", newVersion)
        if (newVersion != null && !lastTag.equals(newVersion)) {
            setGitTag(newVersion)
            publishTag(newVersion)
            println "New version published: ${newVersion}"
        } else {
            println "New version doesn't publish"
        }
    }

    private String getCurrentGitBranch() {
        def branch = 'git rev-parse --abbrev-ref HEAD'.execute().text.trim()
        logger.info("Current git branch: {}", branch)
        return branch
    }

    private String getLastGitTag() {
        def list= 'git tag -l'.execute().text.tokenize('\n')
        if (list.size() > 0) {
            list.sort()
        } else {
            list.add('v0.0')
        }
        return list.last
    }

    private boolean hasUncommittedChanges() {
        def status = 'git status --porcelain'.execute().text.trim()
        logger.info("Uncommited changes: {}", status)
        return !status.isEmpty()
    }

    private String calculateNewVersion(String branch, String lastTag) {
        def newTag
        def versionParts = lastTag.toString().tokenize(/^v(\d+)\.(\d+)-([a-zA-Z]+)$/)
        logger.info("Version parts: {}", versionParts)

        def major = versionParts[0] as Integer
        logger.info("Major: {}", major)
        def minor = versionParts[1] as Integer
        logger.info("Minor: {}", minor)

        switch (branch) {
            case 'dev':
                minor++;
                newTag = "v${major}.${minor}"
                break
            case 'qa':
                minor++;
                newTag = "v${major}.${minor}"
                break
            case 'stage':
                minor++;
                newTag = "v${major}.${minor}-rc"
                break
            case 'master':
                major++;
                newTag = "v${major}.0"
                break
            case 'main':
                major++;
                newTag = "v${major}.0"
                break
            default:
                newTag = "v${major}.${minor}-SNAPSHOT"
        }
        logger.info("Major: {}", major)
        logger.info("Minor: {}", minor)
        return newTag
    }

    private void setGitTag(String newVersion) {
        "git tag $newVersion".execute()
    }

    private void publishTag(String newVersion) {
        "git push origin $newVersion".execute()
    }
}

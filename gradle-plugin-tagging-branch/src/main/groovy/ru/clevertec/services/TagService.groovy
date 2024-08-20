package ru.clevertec.services

import org.gradle.api.Project

class TagService {

    Project project

    TagService(Project project) {
        this.project = project
    }

    String getCurrentGitBranch() {
        return 'git rev-parse --abbrev-ref HEAD'.execute().text.trim()
    }

    String getLastTagFromGit() {
        def list= 'git tag -l'.execute().text.tokenize('\n')
        if (list.size() > 0) {
            list.sort()
        }
        if (list.isEmpty()) {
            list.add('v0.0')
        }
        return list.last
    }

    String calculateNewVersion(String branch, String lastTag) {
        def newTag
        def versionParts = lastTag.toString().tokenize(/^v(\d+)\.(\d+)-([a-zA-Z]+)$/)

        def major = versionParts[0] as Integer
        def minor = versionParts[1] as Integer

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
        return newTag
    }

    boolean hasTag(String tag) {
        def output = "git tag".execute().text.trim().split('\n')
        return output.contains(tag)
    }

    void setGitTag(String newVersion) {
        "git tag $newVersion".execute()
    }

    void publishTag(String newVersion) {
        "git push origin $newVersion".execute()
    }
}

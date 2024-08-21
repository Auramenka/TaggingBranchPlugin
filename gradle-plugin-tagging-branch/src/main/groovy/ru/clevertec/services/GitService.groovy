package ru.clevertec.services

class GitService {

    boolean isGitInstalled() {
        def process = 'git --version'.execute()
        return process.waitFor() == 0
    }

    boolean isRemoteRepositoryAvailable() {
        return 'git remote -v'.execute().text.contains('origin')
    }

    boolean hasUncommittedChanges() {
        def status = 'git status --porcelain'.execute().text.trim()
        return !status.isEmpty()
    }

    String getCurrentGitBranch() {
        return 'git rev-parse --abbrev-ref HEAD'.execute().text.trim()
    }

    void setGitTag(String newVersion) {
        "git tag $newVersion".execute()
    }

    void publishTag(String newVersion) {
        "git push origin $newVersion".execute()
    }

    String calculateNewVersion(String branch, String lastTag) {
        def newTag
        def versionParts = lastTag.toString().tokenize(/^v(\d+)\.(\d+)-([a-zA-Z]+)$/)

        def major = versionParts[0] as Integer
        def minor = versionParts[1] as Integer

        switch (branch) {
            case ~/dev|qa/:
                minor++;
                newTag = "v${major}.${minor}"
                break
            case 'stage':
                minor++;
                newTag = "v${major}.${minor}-rc"
                break
            case ~/master|main/:
                major++;
                newTag = "v${major}.0"
                break
            default:
                newTag = "v${major}.${minor}-SNAPSHOT"
        }
        return newTag
    }
}

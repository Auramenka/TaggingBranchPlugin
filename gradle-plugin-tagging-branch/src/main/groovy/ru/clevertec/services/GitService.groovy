package ru.clevertec.services

import org.gradle.api.Project

class GitService {

    Project project

    GitService(Project project) {
        this.project = project
    }

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
}

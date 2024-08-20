package ru.clevertec.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.clevertec.services.GitService

class CheckGitTask extends DefaultTask {

    @TaskAction
    void checkGit() {
        def gitService = new GitService(project)
        if (!gitService.isGitInstalled()) {
            println "Git is not installed!"
        }

        if (!gitService.isRemoteRepositoryAvailable()) {
            println "No linked remote repository"
        }

        if (gitService.hasUncommittedChanges()) {
            println "Build number: ${project.version}.uncommited"
        }
    }
}

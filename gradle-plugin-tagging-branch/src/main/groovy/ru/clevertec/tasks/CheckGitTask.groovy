package ru.clevertec.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import ru.clevertec.extension.VersionExtension
import ru.clevertec.services.GitService

class CheckGitTask extends DefaultTask {

    @Input
    def gitService = new GitService()

    @TaskAction
    void checkGit() {
        if (!gitService.isGitInstalled()) {
            println "Git is not installed!"
            throw new RuntimeException("Git is not installed")
        }

        if (!gitService.isRemoteRepositoryAvailable()) {
            println "No linked remote repository"
            throw new RuntimeException("No linked remote repository")
        }

        if (gitService.hasUncommittedChanges()) {
            def extension = project.extensions.getByType(VersionExtension)
            def currentVersion = extension.currentVersion
            println "Build number: ${currentVersion}.uncommited"
            throw new RuntimeException("There are uncommited changes")
        }
    }
}

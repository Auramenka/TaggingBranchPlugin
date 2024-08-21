package ru.clevertec.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import ru.clevertec.extension.VersionExtension
import ru.clevertec.services.GitService
import ru.clevertec.services.TagService

class LatestAndNewVersionTask extends DefaultTask {

    @Input
    def tagService = new TagService()

    @Input
    def gitService = new GitService()

    @TaskAction
    void getLatestAndGenerateNewVersion() {
        def lastTag = tagService.getLastTagFromGit()
        println "Latest version: ${lastTag}"

        def branch = gitService.getCurrentGitBranch()
        def newVersion = gitService.calculateNewVersion(branch, lastTag)

        def extension = project.extensions.getByType(VersionExtension)
        extension.currentVersion = newVersion
        println "New version: ${extension.currentVersion}"
    }
}

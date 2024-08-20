package ru.clevertec.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.clevertec.services.TagService

class LatestAndNewVersionTask extends DefaultTask {

    @TaskAction
    void getLatestAndGenerateNewVersion() {
        def tagService = new TagService(project)

        def lastTag = tagService.getLastTagFromGit()
        println "Latest version: ${lastTag}"

        def branch = tagService.getCurrentGitBranch()
        def newVersion = tagService.calculateNewVersion(branch, lastTag)

        project.version = newVersion
        println "New version: ${newVersion}"
    }
}

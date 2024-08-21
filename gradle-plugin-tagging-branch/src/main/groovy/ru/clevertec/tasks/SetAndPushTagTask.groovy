package ru.clevertec.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import ru.clevertec.extension.VersionExtension
import ru.clevertec.services.GitService
import ru.clevertec.services.TagService

class SetAndPushTagTask extends DefaultTask {

    @Input
    def tagService = new TagService()

    @Input
    def gitService = new GitService()

    @TaskAction
    void setAndPushTag() {
        def extension = project.extensions.getByType(VersionExtension)
        def currentVersion = extension.currentVersion

        if (!tagService.hasTag(currentVersion)) {
            gitService.setGitTag(currentVersion)
            gitService.publishTag(currentVersion)
            println "Tag ${currentVersion} set and pushed"
        } else {
            println "Tag ${currentVersion} already exists"
            throw new RuntimeException("Tag already exists")
        }
    }
}

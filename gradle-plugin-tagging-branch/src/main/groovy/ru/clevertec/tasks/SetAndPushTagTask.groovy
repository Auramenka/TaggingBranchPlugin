package ru.clevertec.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.clevertec.services.TagService

class SetAndPushTagTask extends DefaultTask {

    @TaskAction
    void setAndPushTag() {
        def tagService = new TagService(project)
        def currentVersion = project.version

        if (!tagService.hasTag(currentVersion)) {
            tagService.setGitTag(currentVersion)
            tagService.publishTag(currentVersion)
            println "Tag ${currentVersion} set and pushed"
        } else {
            println "Tag ${currentVersion} already exists"
        }
    }
}

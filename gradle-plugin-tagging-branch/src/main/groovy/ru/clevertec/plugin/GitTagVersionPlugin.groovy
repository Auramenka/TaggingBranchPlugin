package ru.clevertec.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.clevertec.task.GitTagVersionTask

class GitTagVersionPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        target.tasks.register("tagVersion", GitTagVersionTask)
    }
}

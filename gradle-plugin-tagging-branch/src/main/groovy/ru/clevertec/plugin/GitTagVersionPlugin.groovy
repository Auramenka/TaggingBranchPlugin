package ru.clevertec.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.clevertec.tasks.CheckGitTask
import ru.clevertec.tasks.LatestAndNewVersionTask
import ru.clevertec.tasks.SetAndPushTagTask

class GitTagVersionPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        target.tasks.create('checkGit', CheckGitTask)
        target.tasks.create('latestAndNewVersion', LatestAndNewVersionTask).dependsOn('checkGit')
        target.tasks.create('createTag', SetAndPushTagTask).dependsOn('latestAndNewVersion')
    }
}

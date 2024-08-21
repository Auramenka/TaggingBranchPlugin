package ru.clevertec.services

class TagService {

    String getLastTagFromGit() {
        def list= 'git tag -l'.execute().text.tokenize('\n')
        if (list.size() > 0) {
            list.sort()
        }
        if (list.isEmpty()) {
            list.add('v0.0')
        }
        return list.last
    }

    boolean hasTag(String tag) {
        def output = "git tag".execute().text.trim().split('\n')
        return output.contains(tag)
    }
}

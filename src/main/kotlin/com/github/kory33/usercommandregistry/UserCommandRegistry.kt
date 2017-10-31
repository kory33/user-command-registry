package com.github.kory33.usercommandregistry

import com.github.kory33.updatenotificationplugin.bukkit.github.GithubUpdateNotifyPlugin

class UserCommandRegistry : GithubUpdateNotifyPlugin() {
    override fun getGithubRepository() = "kory33/user-command-registry"
}
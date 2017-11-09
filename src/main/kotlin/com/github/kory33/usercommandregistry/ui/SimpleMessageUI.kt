package com.github.kory33.usercommandregistry.ui

import com.github.kory33.chatgui.tellraw.MessagePartsList
import com.github.kory33.usercommandregistry.ui.template.ChatInterfaceTemplate
import com.github.kory33.usercommandregistry.util.config.LocaleConfig

class SimpleMessageUI(private val message: String, locale: LocaleConfig, private val withLineBreak: Boolean = true)
    : ChatInterfaceTemplate(locale) {
    override fun constructBodyMassage() = MessagePartsList(message + (if (withLineBreak) "\n" else ""))
}
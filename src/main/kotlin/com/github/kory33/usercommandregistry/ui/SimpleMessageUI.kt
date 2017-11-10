package com.github.kory33.usercommandregistry.ui

import com.github.kory33.chatgui.tellraw.MessagePartsList
import com.github.kory33.usercommandregistry.ui.template.ChatInterfaceTemplate
import com.github.kory33.usercommandregistry.util.config.LocaleConfig

class SimpleMessageUI(message: String, locale: LocaleConfig, withLineBreak: Boolean = true)
    : ChatInterfaceTemplate(locale) {
    override val bodyMassages = MessagePartsList(message + (if (withLineBreak) "\n" else ""))
}
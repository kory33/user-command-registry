package com.github.kory33.usercommandregistry.ui.template

import com.github.kory33.chatgui.model.IChatInterface
import com.github.kory33.chatgui.tellraw.MessagePartsList
import com.github.kory33.usercommandregistry.util.config.LocaleConfig

abstract class ChatInterfaceTemplate(protected val locale: LocaleConfig) : IChatInterface {
    protected abstract val bodyMassages: MessagePartsList

    override fun constructInterfaceMessages(): MessagePartsList {
        val list = MessagePartsList()

        list.addLine(locale["ui.template.header"])
        list.addAll(bodyMassages)
        list.add(locale["ui.template.footer"])

        return list
    }
}
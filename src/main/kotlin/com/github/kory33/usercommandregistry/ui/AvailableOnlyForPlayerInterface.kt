package com.github.kory33.usercommandregistry.ui

import com.github.kory33.chatgui.tellraw.MessagePartsList
import com.github.kory33.usercommandregistry.ui.template.ChatInterfaceTemplate
import com.github.kory33.usercommandregistry.util.config.LocaleConfig

class AvailableOnlyForPlayerInterface(locale: LocaleConfig) : ChatInterfaceTemplate(locale) {
    override fun constructBodyMassage(): MessagePartsList {
        return MessagePartsList(locale.getString("ui.message.player_only"))
    }
}
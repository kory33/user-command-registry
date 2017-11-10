package com.github.kory33.usercommandregistry.ui.template

import com.github.kory33.chatgui.model.player.IPlayerClickableChatInterface
import com.github.kory33.chatgui.util.collection.BijectiveHashMap
import com.github.kory33.usercommandregistry.UserCommandRegistry
import com.github.ucchyocean.messaging.tellraw.MessageParts
import org.bukkit.entity.Player

abstract class ClickableInterfaceTemplate(
        override val targetPlayer: Player,
        protected val plugin: UserCommandRegistry
) : IPlayerClickableChatInterface, ChatInterfaceTemplate(plugin.locale) {
    override val runnableInvoker = plugin.runnableInvoker!!
    override fun constructInterfaceMessages() = super<ChatInterfaceTemplate>.constructInterfaceMessages()

    override val buttonIdMapping = BijectiveHashMap<MessageParts, Long>()
    override var isValidSession = true
}
package com.github.kory33.usercommandregistry.ui

import com.github.kory33.chatgui.model.player.IBrowseablePageInterface
import com.github.kory33.chatgui.tellraw.MessagePartsList
import com.github.kory33.usercommandregistry.UserCommandRegistry
import com.github.kory33.usercommandregistry.ui.template.BrowseableInterfaceTemplate
import org.bukkit.entity.Player

class BrowseableMessageUI: BrowseableInterfaceTemplate {
    constructor(player: Player,
                plugin: UserCommandRegistry,
                messageList: List<String>,
                requestIndex: Int = 0,
                pageSize: Int = 6): super(player, plugin, requestIndex) {
        this.messageList = messageList
        this.entryList = ArrayList(messageList.map { MessagePartsList(it) })
        this.entryPerPage = pageSize
    }

    private constructor(oldInterface: BrowseableMessageUI, newIndex: Int) : super(oldInterface, newIndex) {
        this.messageList = oldInterface.messageList
        this.entryList = oldInterface.entryList
        this.entryPerPage = oldInterface.entryPerPage
    }

    private val messageList: List<String>

    override val entryList: ArrayList<MessagePartsList>
    override val heading = MessagePartsList("")
    override val entryPerPage: Int

    override fun yieldPage(pageIndex: Int): IBrowseablePageInterface {
        return BrowseableMessageUI(this, pageIndex)
    }
}
package com.github.kory33.usercommandregistry.ui.template

import com.github.kory33.chatgui.model.player.IBrowseablePageInterface
import com.github.kory33.usercommandregistry.UserCommandRegistry
import org.bukkit.entity.Player

abstract class BrowseableInterfaceTemplate(player: Player,
                                           plugin: UserCommandRegistry,
                                           pageIndex: Int)
    : IBrowseablePageInterface, ClickableInterfaceTemplate(player, plugin) {

    constructor(oldInterface: BrowseableInterfaceTemplate, newIndex: Int)
            : this(oldInterface.targetPlayer, oldInterface.plugin, newIndex)

    override val interfaceManager = plugin.interfaceManager!!
    override val requestedPageIndex = pageIndex

    private fun getNextButtonColor(isActive: Boolean): String {
        return if (isActive) {
            locale["ui.template.browseable.active"]
        } else {
            locale["ui.template.browseable.inactive"]
        }
    }

    override fun getNextButton(isActive: Boolean): String {
        return "${getNextButtonColor(isActive)}${locale["ui.template.browseable.next"]}"
    }

    override fun getPrevButton(isActive: Boolean): String {
        return "${getNextButtonColor(isActive)}${locale["ui.template.browseable.prev"]}"
    }

    override fun getPageDisplayComponent(currentPageNumber: Int, maxPageNumber: Int): String {
        return locale.getFormatted("ui.template.browseable.page", currentPageNumber, maxPageNumber)
    }
}
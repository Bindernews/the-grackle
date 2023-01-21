package io.bindernews.thegrackle.ui

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum
import com.megacrit.cardcrawl.screens.options.ConfirmPopup
import io.bindernews.bnsts.eventbus.EventEmit
import io.bindernews.bnsts.eventbus.IEventEmit

class MyConfirmPopup(title: String?, desc: String?) : ConfirmPopup(title, desc, CO.CUSTOM) {
    val onAction: IEventEmit<Action> = EventEmit()
    override fun show() {
        if (!shown) {
            shown = true
            onAction.emit(Action.SHOW)
        }
    }

    override fun hide() {
        if (shown) {
            shown = false
            onAction.emit(Action.HIDE)
        }
    }

    override fun noButtonEffect() {
        hide()
        onAction.emit(Action.NO)
    }

    override fun yesButtonEffect() {
        hide()
        onAction.emit(Action.YES)
    }

    enum class Action {
        YES, NO, SHOW, HIDE
    }

    object CO {
        @SpireEnum(name = "GRACKLE_CUSTOM")
        var CUSTOM: ConfirmType? = null
    }
}
package net.bindernews.grackle.ui

import basemod.ReflectionHacks
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen
import com.megacrit.cardcrawl.screens.options.ToggleButton
import net.bindernews.grackle.api.IPopup
import net.bindernews.grackle.helper.makeId

class MainMenuMetricsRequest : IPopup {
    private val strings = CardCrawlGame.languagePack.getUIString(ID)
    private val confirmPopup = MyConfirmPopup(strings.TEXT[0], strings.TEXT[1])

    /** True if we've already shown the popup.  */
    var prompted = false

    init {
        confirmPopup.onAction.on { a ->
            if (a == MyConfirmPopup.Action.YES) {
                setUploadData(true)
            }
        }
    }

    override fun render(sb: SpriteBatch) {
        confirmPopup.render(sb)
    }

    override fun update() {
        if (!prompted && !Settings.UPLOAD_DATA) {
            prompted = true
            confirmPopup.show()
        }
        confirmPopup.update()
    }

    override fun isEnabled(): Boolean {
        val screen = CardCrawlGame.mainMenuScreen ?: return false
        return screen.screen == MainMenuScreen.CurScreen.MAIN_MENU
    }

    companion object {
        @JvmField val ID = makeId(MainMenuMetricsRequest::class)
        @JvmStatic val inst by lazy { MainMenuMetricsRequest() }

        fun setUploadData(enabled: Boolean) {
            val panel = CardCrawlGame.mainMenuScreen.optionPanel
            val btn = ReflectionHacks.getPrivate<Any>(panel, panel.javaClass, "uploadToggle") as ToggleButton
            if (btn.enabled != enabled) {
                btn.toggle()
            }
        }
    }
}
package io.bindernews.thegrackle.ui

import basemod.ReflectionHacks
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.HitboxListener
import com.megacrit.cardcrawl.helpers.input.InputHelper
import io.bindernews.thegrackle.GrackleMod
import io.bindernews.thegrackle.api.IPopup
import io.bindernews.thegrackle.helper.makeId
import java.awt.Desktop
import java.awt.Insets
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.util.*

class CardClickableLink : IPopup, HitboxListener {
    val strings = CardCrawlGame.languagePack.getUIString(ID)!!
    private val confirmPopup = MyConfirmPopup(strings.TEXT[0], "")
    private val urlHb = Hitbox(1f, 1f)
    private val font: BitmapFont = FontHelper.tipBodyFont
    private var enabled = false
    private var title: String? = null
    private var url: URI? = null
    private var textH = 0f

    init {
        confirmPopup.onAction.on { a ->
            if (a == MyConfirmPopup.Action.YES) {
                openBrowser()
            }
        }
    }

    fun open(title: String, url: String) {
        try {
            this.url = URI(url)
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
        this.title = title
        enabled = true
        val popup = CardCrawlGame.cardPopup
        val cardHb = ReflectionHacks.getPrivate<Any>(popup, popup.javaClass, "cardHb") as Hitbox
        val nw = (FontHelper.getSmartWidth(font, title, cardHb.width, 1f)
                + padding.left + padding.right)
        textH = FontHelper.getHeight(font, title, 2f)
        val nh = textH + padding.top + padding.bottom
        urlHb.resize(nw, nh)
        urlHb.move(cardHb.cX, cardHb.y + cardHb.height + nh * 2f)
        confirmPopup.desc = url
    }

    fun close() {
        enabled = false
        title = ""
        url = null
    }

    /**
     * Calls [.open] or [.close] depending on the value of `isOpen`.
     * @param title Passed to `open()`
     * @param url Passed to `open()`
     */
    fun openOrClose(title: String, url: String, isOpen: Boolean) {
        if (isOpen) {
            open(title, url)
        } else {
            close()
        }
    }

    override fun render(sb: SpriteBatch) {
        renderSelf(sb)
        confirmPopup.render(sb)
    }

    private fun renderSelf(sb: SpriteBatch) {
        sb.color = BUTTON_COLOR
        val tbox = tooltipBox
        val realW = tbox.totalWidth
        val realH = tbox.totalHeight - tbox.middleHeight
        val scaleW = urlHb.width / realW
        val scaleH = urlHb.height / realH
        tbox.draw(sb, urlHb.x, urlHb.y, 0f, 0f, realW, realH, scaleW, scaleH, 0f)
        // Render highlights
        if (urlHb.hovered) {
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE)
            sb.color = BRIGHTEN
            tbox.draw(sb, urlHb.x, urlHb.y, 0f, 0f, realW, realH, scaleW, scaleH, 0f)
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        }
        FontHelper.renderFontCentered(sb, font, title, urlHb.cX, urlHb.cY, Color.WHITE)
    }

    override fun update() {
        urlHb.encapsulatedUpdate(this)
        confirmPopup.update()
    }

    override fun isEnabled(): Boolean {
        if (!CardCrawlGame.cardPopup.isOpen) {
            enabled = false
        }
        return enabled
    }

    override fun hoverStarted(hitbox: Hitbox) {}
    override fun startClicking(hitbox: Hitbox) {
        // Consume the event
        InputHelper.justClickedLeft = false
    }

    override fun clicked(hitbox: Hitbox) {
        if (hitbox === urlHb) {
            confirmPopup.show()
        }
    }

    fun openBrowser() {
        try {
            Desktop.getDesktop().browse(url)
        } catch (e: IOException) {
            GrackleMod.log.warn("unable to open browser", e)
        }
    }

    companion object {
        @JvmStatic val inst = CardClickableLink()
        @JvmField val ID = makeId(CardClickableLink::class)

        val tooltipBox by lazy {
            val path = GrackleMod.MOD_RES + "/images/ui/tip_box.png"
            val tex = Objects.requireNonNull(GrackleMod.loadTexture(path))
            NinePatch(tex, 80, 80, 32, 32)
        }

        val padding = Insets(8, 16, 8, 16)
        val BUTTON_COLOR = Color(-0x26cfcf01)
        val BRIGHTEN = Color(1f, 1f, 1f, 0.3f)
    }
}
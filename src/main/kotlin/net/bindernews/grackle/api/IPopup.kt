package net.bindernews.grackle.api

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import net.bindernews.eventbus.IPriority

interface IPopup : IPriority<IPopup> {
    fun render(sb: SpriteBatch)
    fun update()
    fun isEnabled(): Boolean
}
package io.bindernews.thegrackle.api

import com.badlogic.gdx.graphics.g2d.SpriteBatch

interface IPopup {
    fun render(sb: SpriteBatch)
    fun update()
    fun isEnabled(): Boolean
}
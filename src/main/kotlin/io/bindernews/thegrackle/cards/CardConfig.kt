package io.bindernews.thegrackle.cards

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.megacrit.cardcrawl.cards.AbstractCard.*
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.CardStrings
import io.bindernews.thegrackle.GrackleMod

/**
 * Card configuration class. Reduces the repetitiveness of creating a card.
 */
class CardConfig(
        val name: String,
        @JvmField val type: CardType,
        @JvmField val rarity: CardRarity,
        @JvmField val target: CardTarget
) {
    @JvmField val ID: String = GrackleMod.MOD_ID + ":" + name

    val strings: CardStrings by lazy { CardCrawlGame.languagePack.getCardStrings(ID) }

    private var _portrait: AtlasRegion? = null
    val portrait: AtlasRegion get() = _portrait!!

    private var _betaPortrait: AtlasRegion? = null
    val betaPortrait: AtlasRegion get() = _betaPortrait!!

    constructor(
            companion: Any,
            type: CardType,
            rarity: CardRarity,
            target: CardTarget
    ) : this(nameFromCompanion(companion), type, rarity, target)

    fun loadImages(atlas: TextureAtlas) {
        if (_portrait == null) {
            _portrait = findFirstRegion(atlas, makeImagePaths(""))
            _betaPortrait = findFirstRegion(atlas, makeImagePaths("_b"))
            if (_betaPortrait == null) {
                _betaPortrait = _portrait
            }
        }
    }

    private fun makeImagePaths(suffix: String): Array<String> {
        return arrayOf(
                name + suffix,
                typeToName(type) + suffix)
    }

    companion object {
        private fun nameFromCompanion(companion: Any): String {
            return if (companion is Class<*>) {
                if (BaseCard::class.java.isAssignableFrom(companion)) {
                    return companion.simpleName
                }
                val outerClass = companion.enclosingClass
                if (outerClass != null) {
                    return nameFromCompanion(outerClass)
                }
                throw RuntimeException("unable to determine class name from " + companion.name)
            } else {
                nameFromCompanion(companion.javaClass)
            }
        }

        private fun findFirstRegion(atlas: TextureAtlas, names: Array<String>): AtlasRegion? {
            for (name in names) {
                val r = atlas.findRegion(name)
                if (r != null) {
                    return r
                }
            }
            return null
        }

        private fun typeToName(type: CardType?): String {
            return when (type) {
                CardType.ATTACK -> "Attack"
                CardType.SKILL -> "Skill"
                CardType.POWER -> "Power"
                CardType.STATUS -> "Status"
                CardType.CURSE -> "Curse"
                else -> ""
            }
        }
    }
}
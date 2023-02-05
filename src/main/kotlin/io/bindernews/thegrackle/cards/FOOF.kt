package io.bindernews.thegrackle.cards

import basemod.cardmods.ExhaustMod
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.Events
import io.bindernews.thegrackle.helper.ModInterop.Companion.iop
import io.bindernews.thegrackle.power.BurningPower
import io.bindernews.thegrackle.ui.CardClickableLink

@Suppress("MemberVisibilityCanBePrivate")
class FOOF : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val amt = magicNumber
        iop().getFriends(p).forEach { addToBot(BurningPower.makeAction(p, it, amt)) }
        iop().getEnemies(p).forEach { addToBot(BurningPower.makeAction(p, it, amt)) }
    }

    companion object {
        @JvmStatic val C = CardConfig("FOOF", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL)
        val VARS = CardVariables.config { c ->
            c.cost(1)
            c.magic(10, 16)
            c.addModifier(ExhaustMod())
        }

        const val ABOUT_TITLE = "Things I Won't Work With: Dioxygen Difluoride"
        const val ABOUT_URL = "https://www.science.org/content/blog-post/things-i-won-t-work-dioxygen-difluoride"

        init {
            Events.svcCardChange.on { ev ->
                if (ev.card is FOOF) {
                    CardClickableLink.inst.openOrClose(ABOUT_TITLE, ABOUT_URL, ev.open)
                }
            }
        }
    }
}
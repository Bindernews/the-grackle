package net.bindernews.grackle.cards

import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.actions.animations.VFXAction
import com.megacrit.cardcrawl.actions.common.InstantKillAction
import com.megacrit.cardcrawl.actions.utility.WaitAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder
import net.bindernews.grackle.helper.speedBoost
import net.bindernews.grackle.power.SpeedPower
import net.bindernews.grackle.vfx.MyGiantTextEffect

class Death : BaseCard(C, VARS) {
    override val descriptionSource get() = descriptionBuilder

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        if (!SpeedPower.tryBoost(p, this)) {
            return
        }
        if (m != null) {
            addToBot(VFXAction(WeightyImpactEffect(m.hb.cX, m.hb.cY, Color.GOLD.cpy())))
            addToBot(WaitAction(0.8F))
            addToBot(VFXAction(MyGiantTextEffect(m.hb.cX, m.hb.cY, C.strings.EXTENDED_DESCRIPTION[0])))

            if (!m.isDeadOrEscaped && m.currentHealth <= magicNumber) {
                addToBot(InstantKillAction(m))
            }
        }
    }

    override fun canUse(p: AbstractPlayer, m: AbstractMonster?): Boolean {
        if (!SpeedPower.canBoost(p, this)) {
            cantUseMessage = SpeedPower.needMoreSpeed
            return false
        }
        return super.canUse(p, m)
    }

    override fun isBonusActive(): Boolean = isBonusActive(owner!!)

    companion object {
        @JvmField val C = CardConfig("Death", CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY)
        val VARS = CardVariables().apply {
            cost(0)
            magic(40, 60)
            speedBoost(20, -1)
            onInit {
                it.exhaust = true
            }
        }

        val descriptionBuilder = DescriptionBuilder.create {
            val judgementDesc = CardCrawlGame.languagePack.getCardStrings("Judgement").DESCRIPTION
            judgementDesc + format(" NL {Exhaust}. NL {speed_boost} {Required}.")
        }
    }
}
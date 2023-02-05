package io.bindernews.thegrackle.stance

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.stances.AbstractStance
import io.bindernews.thegrackle.GrackleMod
import io.bindernews.thegrackle.helper.ModInterop.Companion.iop
import java.util.*

class StanceAloft : AbstractStance() {
    /**
     * Used for temporary damage calculations.
     */
    var enabled = true

    init {
        ID = STANCE_ID
        name = STRINGS.NAME
        updateDescription()
    }

    override fun atDamageReceive(damage: Float, damageType: DamageType): Float {
        return if (damageType == DamageType.NORMAL && enabled) {
            damage / 2f
        } else {
            damage
        }
    }

    override fun atDamageGive(damage: Float, damageType: DamageType): Float {
        return if (damageType == DamageType.NORMAL && enabled) {
            damage / 2f
        } else {
            damage
        }
    }

    override fun updateDescription() {
        description = STRINGS.DESCRIPTION[0]
    }

    companion object {
        @JvmField val STANCE_ID = GrackleMod.makeId(StanceAloft::class.java)
        @JvmStatic val STRINGS = CardCrawlGame.languagePack.getStanceString(STANCE_ID)

        /**
         * List of stance IDs that count as "aloft".
         */
        @JvmStatic val ALOFT_STANCES: MutableList<String> = ArrayList()

        init {
            ALOFT_STANCES.add(STANCE_ID)
            ALOFT_STANCES.add(StancePhoenix.STANCE_ID)
        }

        /**
         * If the player is aloft then returns true, otherwise sets `cantUseMessage` and returns false.
         * @param p player
         * @param card the card to update
         */
        @Suppress("UNUSED_PARAMETER")
        fun checkPlay(card: AbstractCard, p: AbstractPlayer, ignoredM: AbstractMonster?): Boolean {
            val b = isAloft(p)
            if (!b) {
                card.cantUseMessage = STRINGS.DESCRIPTION[1]
            }
            return b
        }

        fun isAloft(p: AbstractCreature?): Boolean {
            if (p == null) {
                return false
            }
            return iop().getStance(p)?.let { isAloft(it) } ?: false
        }

        fun isAloft(s: AbstractStance): Boolean {
            return ALOFT_STANCES.contains(s.ID)
        }

        fun getInstanceOn(c: AbstractCreature): Optional<StanceAloft> {
            return Optional.ofNullable(iop().getStance(c)).map { st -> st as? StanceAloft }
        }
    }
}
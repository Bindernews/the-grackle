package net.bindernews.grackle.power

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.Settings.GameLanguage
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.localization.PowerStrings
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.helper.DescriptionBuilder
import net.bindernews.grackle.helper.ISaveStrings
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import java.lang.RuntimeException

class EmbodyFirePower(owner: AbstractCreature, amount: Int) : BasePower(POWER_ID) {
    init {
        setOwnerAmount(owner, amount)
        loadRegion("flameBarrier")
        updateDescription()
    }

    override fun atStartOfTurnPostDraw() {
        // Basically copied from NoxiousFumesPower
        val monsters = AbstractDungeon.getMonsters()
        if (!monsters.areMonstersBasicallyDead()) {
            flash()
            for (m in monsters.monsters) {
                if (!m.isDead && !m.isDying) {
                    addToBot(iop().actionApplyPower(owner, m, BurningPower.POWER_ID, amount))
                }
            }
        }
    }

    override fun updateDescription() {
        description = rawDescription.format(amount)
    }

    companion object : ISaveStrings<PowerStrings> {
        @JvmField val POWER_ID = GrackleMod.makeId(EmbodyFirePower::class.java)

        val rawDescription = DescriptionBuilder.create()
            .atStartOfTurn()
            .applyPower("#b%d", "#yBurning")
            .toAllEnemies()
            .period(false)
            .build()

        override fun saveStrings(lang: GameLanguage): PowerStrings = PowerStrings().apply {
            NAME = when (lang) {
                GameLanguage.ENG -> "Embody Fire"
                else -> throw RuntimeException("unsupported language")
            }
            DESCRIPTIONS = arrayOf(rawDescription)
        }
    }
}
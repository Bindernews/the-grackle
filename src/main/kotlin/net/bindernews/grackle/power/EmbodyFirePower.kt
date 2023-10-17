package net.bindernews.grackle.power

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.helper.DescriptionBuilder
import net.bindernews.grackle.helper.ModInterop.Companion.iop

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
        description = descriptionBuilder.get(false).format(amount)
    }

    companion object {
        @JvmField val POWER_ID = GrackleMod.makeId(EmbodyFirePower::class.java)

        val descriptionBuilder = DescriptionBuilder.create {
            when (lang) {
                else -> format("{+start_of_turn} apply #b%d #yBurning to ALL enemies.")
            }
        }
    }
}
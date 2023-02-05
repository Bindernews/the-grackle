package net.bindernews.grackle.power

import basemod.AutoAdd
import basemod.interfaces.CloneablePowerInterface
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.PowerStrings
import com.megacrit.cardcrawl.powers.AbstractPower
import net.bindernews.grackle.GrackleMod

@AutoAdd.Ignore
abstract class BasePower(id: String) : AbstractPower(), CloneablePowerInterface {
    val strings: PowerStrings

    init {
        ID = id
        strings = CardCrawlGame.languagePack.getPowerStrings(ID)
        name = strings.NAME
    }

    protected fun setOwnerAmount(owner: AbstractCreature, amount: Int) {
        this.owner = owner
        this.amount = amount
    }

    override fun loadRegion(fileName: String) {
        val k48 = "48/$fileName"
        val k128 = "128/$fileName"
        region48 = powerAtlas.findRegion(k48) ?: atlas.findRegion(k48)
        region128 = powerAtlas.findRegion(k128) ?: atlas.findRegion(k128)
    }

    /**
     * Convenience method to use the indexed `DESCRIPTIONS` string
     * in [String.format], passing the rest of the arguments.
     * @param index Index of the description string
     * @param args Format arguments
     * @return Formatted string
     */
    fun formatDesc(index: Int, vararg args: Any): String {
        return String.format(strings.DESCRIPTIONS[index], *args)
    }

    override fun makeCopy(): AbstractPower {
        return try {
            javaClass.getConstructor(AbstractCreature::class.java, Int::class.javaPrimitiveType)
                .newInstance(owner, amount)
        } catch (ex: Exception) {
            throw UnsupportedOperationException("cannot automatically clone power ${javaClass.name}")
        }
    }

    companion object {
        val powerAtlas by lazy { TextureAtlas(GrackleMod.CO.RES_IMAGES + "/powers/powers.atlas") }
    }
}
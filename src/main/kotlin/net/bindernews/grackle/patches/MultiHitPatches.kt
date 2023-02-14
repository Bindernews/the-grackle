@file:Suppress("FunctionName", "ClassName", "LocalVariableName")

package net.bindernews.grackle.patches

import basemod.abstracts.AbstractCardModifier
import basemod.helpers.CardModifierManager
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.unique.MulticastAction
import com.megacrit.cardcrawl.actions.unique.SkewerAction
import com.megacrit.cardcrawl.actions.unique.WhirlwindAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.blue.Blizzard
import com.megacrit.cardcrawl.cards.blue.MultiCast
import com.megacrit.cardcrawl.cards.blue.ThunderStrike
import com.megacrit.cardcrawl.cards.green.DaggerSpray
import com.megacrit.cardcrawl.cards.green.Eviscerate
import com.megacrit.cardcrawl.cards.green.RiddleWithHoles
import com.megacrit.cardcrawl.cards.green.Skewer
import com.megacrit.cardcrawl.cards.red.Pummel
import com.megacrit.cardcrawl.cards.red.SwordBoomerang
import com.megacrit.cardcrawl.cards.red.Whirlwind
import com.megacrit.cardcrawl.characters.AbstractPlayer
import javassist.CannotCompileException
import javassist.CtBehavior
import javassist.CtClass
import javassist.bytecode.CodeAttribute
import javassist.bytecode.CodeIterator
import javassist.bytecode.LineNumberAttribute
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.api.IMultiHitManager
import net.bindernews.grackle.helper.ExtraHitModifier.*
import net.bindernews.grackle.helper.extraHits
import java.nio.Buffer
import java.nio.ByteBuffer

@Suppress("unused")
object MultiHitPatches {
    const val CTOR = "<ctor>"
    private val damageModifiers = hashMapOf<Class<*>, AbstractDamageModifier>(
        Eviscerate::class.java to SimpleExtraHits(AbstractGameAction.AttackEffect.SLASH_HEAVY),
        DaggerSpray::class.java to DaggerSprayMod(),
    )
    private val cardModifiers = hashMapOf<Class<*>, AbstractCardModifier>(
        SwordBoomerang::class.java to MagicNumberMod(),
        Pummel::class.java to MagicNumberMod(),
        RiddleWithHoles::class.java to RiddleWithHolesMod(),
    )

    @JvmStatic fun manager(): IMultiHitManager {
        return GrackleMod.multiHitManager
    }

    @SpirePatch2(clz = Blizzard::class, method = "use")
    object patchBlizzard {
        @SpireInsertPatch(rloc = 49 - 47, localvars = ["frostCount"])
        @JvmStatic fun Insert(__instance: Blizzard, @ByRef frostCount: IntArray) {
            frostCount[0] += manager().getExtraHitsCard(__instance, frostCount[0])
        }
    }

    @SpirePatch2(clz = ThunderStrike::class, method = "use")
    object patchThunderStrikeUse {
        @SpireInsertPatch(rloc = 37 - 36)
        @JvmStatic fun Insert(__instance: ThunderStrike) {
            __instance.baseMagicNumber += manager().getExtraHitsCard(__instance, __instance.baseMagicNumber)
        }
    }
    @SpirePatch2(clz = ThunderStrike::class, method = "applyPowers")
    object patchThunderStrikeApplyPowers {
        @SpireInsertPatch(rloc = 64 - 60)
        @JvmStatic fun Insert(__instance: ThunderStrike) {
            __instance.baseMagicNumber += manager().getExtraHitsCard(__instance, __instance.baseMagicNumber)
        }
    }

    @SpirePatch2(clz = RiddleWithHoles::class, method = "use")
    object patchRiddleWithHoles {
        @JvmStatic fun Raw(method: CtBehavior) {
            val ca = method.methodInfo2.codeAttribute
            // Add local variable to hold hit count
            val newLine = insertLine(ca, 0)
            method.addLocalVariable("hits", CtClass.intType)
            method.insertAt(newLine, "hits = this.magicNumber;")
            // Find target
            val iter = ca.iterator()
            var pos = findCode(iter, shortArrayOf(0x1d, 0x08, 0xa2)) ?:
                throw CannotCompileException("unable to patch RiddleWithHoles\n" + hexDump(iter.get().code))
            // Change loop condition to compare to local variable
            pos += 1
            iter.insertGap(pos, 1)
            iter.write16bit(0x1504, pos)
        }
    }

    @SpirePatches(
        SpirePatch(clz = WhirlwindAction::class, method = "update"),
        SpirePatch(clz = SkewerAction::class, method = "update"),
        SpirePatch(clz = MulticastAction::class, method = "update"),
    )
    object patchXCostActions {
        @JvmStatic fun Raw(method: CtBehavior) {
            patchXCostAction(method, "effect", "p")
        }
    }

    internal fun patchXCostAction(method: CtBehavior, localName: String, playerField: String) {
        val lines = LineFinder.findInOrder(method, Matcher.MethodCallMatcher(AbstractPlayer::class.java, "hasRelic"))
        val managerRef = "${MultiHitPatches::class.java.name}.manager()"
        val code = "$localName += $managerRef.getExtraHits(this.$playerField, $localName);"
        method.insertAt(lines[0], true, code)
    }

    /**
     * Helper to dump raw bytecode for Raw spire patches
     */
    fun hexDump(ar: ByteArray): String {
        return ar.joinToString(separator = " ") { "%02x".format(it) }
    }

    /**
     * Helper function to locate raw bytecode
     */
    fun findCode(iter: CodeIterator, target: ShortArray): Int? {
        outer@ while (iter.hasNext()) {
            val pos = iter.next()
            for (i in target.indices) {
                val tmp = iter.byteAt(pos + i).toShort()
                if (tmp != target[i]) {
                    continue@outer
                }
            }
            return pos
        }
        return null
    }

    fun insertLine(ca: CodeAttribute, pc: Int): Int {
        val lines = ca.getAttribute(LineNumberAttribute.tag) as LineNumberAttribute
        val lineInfo = lines.toNearPc(lines.toLineNumber(pc))
        // Check that there's not already a line entry for this PC
        if (lines.startPc(lineInfo.index) == pc) {
            return lineInfo.line
        }
        // Add new entry to line list
        val buf = ByteBuffer.allocate(lines.get().size + 4)
        buf.put(lines.get())
        buf.putShort(pc.toShort())
        buf.putShort(lineInfo.line.toShort())
        (buf as Buffer).rewind()
        val sbuf = buf.asShortBuffer()
        sbuf.put(0, (sbuf[0] + 1).toShort())
//        println(hexDump(lines.get()))
        for (i in 2 until sbuf.limit() step 2) {
            val a = sbuf[i]
            if (a < lineInfo.line) {
                sbuf.put(i, (a + 1).toShort())
            }
        }
        lines.set(buf.array())
//        println(hexDump(lines.get()))
        return lineInfo.line + 1
    }

    open class MagicNumberMod(private val initial: Int = -1) : AbstractCardModifier() {
        override fun onInitialApplication(card: AbstractCard) {
            if (initial != -1) {
                card.baseMagicNumber = initial
                card.magicNumber = initial
            }
        }

        override fun onApplyPowers(card: AbstractCard) {
            card.magicNumber += card.extraHits
        }

        override fun makeCopy(): AbstractCardModifier = MagicNumberMod(initial)
    }

    class RiddleWithHolesMod : MagicNumberMod(5) {
        override fun modifyDescription(rawDescription: String, card: AbstractCard): String {
            return rawDescription.replace("5", "!M!")
        }

        override fun makeCopy(): AbstractCardModifier = RiddleWithHolesMod()
    }

    @SpirePatches(
        SpirePatch(clz = Blizzard::class, method = CTOR),
        SpirePatch(clz = ThunderStrike::class, method = CTOR),
        SpirePatch(clz = Whirlwind::class, method = CTOR),
        SpirePatch(clz = SwordBoomerang::class, method = CTOR),
        SpirePatch(clz = Eviscerate::class, method = CTOR),
        SpirePatch(clz = RiddleWithHoles::class, method = CTOR),
        SpirePatch(clz = Skewer::class, method = CTOR),
        SpirePatch(clz = MultiCast::class, method = CTOR),
    )
    object TagMultiHitInternal {
        @JvmStatic fun Postfix(__instance: AbstractCard) {
            manager().makeMultiHit(__instance)
            damageModifiers[__instance.javaClass]?.let { DamageModifierManager.addModifier(__instance, it.makeCopy()) }
            cardModifiers[__instance.javaClass]?.let { CardModifierManager.addModifier(__instance, it.makeCopy()) }
        }
    }
}
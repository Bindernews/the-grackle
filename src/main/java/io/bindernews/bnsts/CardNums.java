package io.bindernews.bnsts;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.evacipated.cardcrawl.mod.stslib.variables.RefundVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import io.bindernews.thegrackle.variables.ExtraHitsVariable;
import lombok.*;

import java.lang.invoke.*;

/**
 * Holds commonly configured numbers for cards, and has a builder to make it easy to keep
 * all the card "numbers" in one place. Also includes an upgrade function for convenience.
 */
@Builder
public class CardNums {
    private static MethodHandle hUpgradeDamage;
    private static MethodHandle hUpgradeBlock;
    private static MethodHandle hUpgradeMagic;
    private static MethodHandle hUpgradeName;
    private static MethodHandle hUpgradeCost;

    static {
        val mtypeInt = MethodType.methodType(void.class, int.class);
        val mtypeNone = MethodType.methodType(void.class);
        hUpgradeDamage = findCardMethod("upgradeDamage", mtypeInt);
        hUpgradeBlock = findCardMethod("upgradeBlock", mtypeInt);
        hUpgradeMagic = findCardMethod("upgradeMagicNumber", mtypeInt);
        hUpgradeCost = findCardMethod("upgradeBaseCost", mtypeInt);
        hUpgradeName = findCardMethod("upgradeName", mtypeNone);
    }

    private static MethodHandle findCardMethod(String name, MethodType args) {
        try {
             val m = AbstractCard.class.getDeclaredMethod(name, args.parameterArray());
             m.setAccessible(true);
             return MethodHandles.lookup().unreflect(m);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Builder.Default public final int cost = 0;
    @Builder.Default public final int costUpg = -1;
    @Builder.Default public final int damage = -1;
    @Builder.Default public final int damageUpg = -1;
    @Builder.Default public final int block = -1;
    @Builder.Default public final int blockUpg = -1;
    @Builder.Default public final int magic = -1;
    @Builder.Default public final int magicUpg = -1;
    @Builder.Default public final int heal = -1;
    @Builder.Default public final int healUpg = -1;
    @Builder.Default public final int exhaustive = -1;
    @Builder.Default public final int exhaustiveUpg = -1;
    @Builder.Default public final int refund = -1;
    @Builder.Default public final int refundUpg = -1;
    @Builder.Default public final int extraHits = -1;
    @Builder.Default public final int extraHitsUpg = -1;

    public void init(CustomCard card) {
        card.damage = card.baseDamage = damage;
        card.block = card.baseBlock = block;
        card.magicNumber = card.baseMagicNumber = magic;
        card.heal = card.baseHeal = heal;
        card.cost = card.costForTurn = cost;
        if (exhaustive != -1) {
            ExhaustiveVariable.setBaseValue(card, exhaustive);
        }
        if (refund != -1) {
            RefundVariable.setBaseValue(card, refund);
        }
        if (extraHits != -1) {
            ExtraHitsVariable.inst.setBaseValue(card, extraHits);
        }
    }

    @SneakyThrows
    public void upgrade(CustomCard card, boolean allowMulti) {
        if (card.upgraded && !allowMulti) {
            return;
        }
        hUpgradeName.invoke(card);
        if (costUpg != -1) {
            hUpgradeCost.invoke(card, costUpg);
        }
        if (damageUpg != -1) {
            hUpgradeDamage.invoke(card, damageUpg - damage);
        }
        if (blockUpg != -1) {
            hUpgradeBlock.invoke(card, blockUpg - block);
        }
        if (magicUpg != -1) {
            hUpgradeMagic.invoke(card, magicUpg - magic);
        }
        if (exhaustiveUpg != -1) {
            ExhaustiveVariable.upgrade(card, exhaustiveUpg - exhaustive);
        }
        if (refundUpg != -1) {
            RefundVariable.upgrade(card, refundUpg - refund);
        }
        if (extraHitsUpg != -1) {
            ExtraHitsVariable.inst.upgrade(card, extraHitsUpg - extraHits);
        }
    }
}

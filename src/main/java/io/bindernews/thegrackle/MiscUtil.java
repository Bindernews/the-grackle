package io.bindernews.thegrackle;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.Arrays;

public class MiscUtil {

    /** Returns a new array list containing the given elements. */
    @SafeVarargs
    public static <E> ArrayList<E> arrayListOf(E ...elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }

    public static String cardImagePath(Class<?> clazz) {
        return Const.RES_IMAGES + "/card/" + clazz.getName() + ".png";
    }


    /**
     * Returns either a {@link RemoveSpecificPowerAction} or {@link ReducePowerAction}
     * depending on the current amount of the power.
     *
     * @param p Power instance to decrease
     * @param amount Amount to decrease by
     */
    public static AbstractGameAction getLowerPowerAction(AbstractPower p, int amount) {
        if (p.amount < amount) {
            return new RemoveSpecificPowerAction(p.owner, p.owner, p.ID);
        } else {
            return new ReducePowerAction(p.owner, p.owner, p.ID, amount);
        }
    }

    /**
     * Add an action to the bottom of the stack, convenience function.
     */
    public static void addToBot(AbstractGameAction a) {
        AbstractDungeon.actionManager.addToBottom(a);
    }

    public static void powerInit(
            AbstractPower p, AbstractCreature owner, int amount, String id, PowerStrings strings
    ) {
        p.ID = id;
        p.name = strings.NAME;
        p.owner = owner;
        p.amount = amount;
    }
}

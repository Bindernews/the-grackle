package io.bindernews.thegrackle.power;

import charbosses.actions.unique.EnemyChangeStanceAction;
import charbosses.bosses.AbstractCharBoss;
import charbosses.stances.AbstractEnemyStance;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.stance.StancePhoenix;

/**
 * Implements most of the features of PhoenixStance.
 */
public class PhoenixStancePower extends BasePower implements InvisiblePower {
    public static final String POWER_ID = GrackleMod.makeId(PhoenixStancePower.class);

    public PhoenixStancePower(AbstractCreature owner) {
        super(POWER_ID);
        setOwnerAmount(owner, -1);
        type = PowerType.BUFF;
        isTurnBased = false;
    }

    @Override
    public void onInitialApplication() {
        addToBot(getStanceAction(StancePhoenix.STANCE_ID));
    }

    @Override
    public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
        if (!StancePhoenix.is(newStance)) {
            addToBot(getStanceAction(StancePhoenix.STANCE_ID));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
            addToBot(new ApplyPowerAction(owner, owner, new CoolingPhoenixPower(owner, 1)));
            addToBot(getStanceAction("Neutral"));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new PhoenixStancePower(owner);
    }

    public AbstractGameAction getStanceAction(String stance) {
        if (owner instanceof AbstractPlayer) {
            AbstractStance s = AbstractStance.getStanceFromName(stance);
            return new ChangeStanceAction(s);
        } else {
            // support downfall mod
            return null;
        }
    }

    @SpirePatch(clz = PhoenixStancePower.class, method = "getStanceAction", requiredModId = "downfall")
    public static class DownfallInterop {
        @SpireInsertPatch(rloc = 62-57)
        public static SpireReturn<AbstractGameAction> Insert(PhoenixStancePower __instance, String stance) {
            if (__instance.owner instanceof AbstractCharBoss) {
                AbstractGameAction ac = new EnemyChangeStanceAction(AbstractEnemyStance.getStanceFromName(stance));
                return SpireReturn.Return(ac);
            } else {
                return SpireReturn.Continue();
            }
        }
    }
}

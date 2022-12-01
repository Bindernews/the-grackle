package io.bindernews.thegrackle.power;

import charbosses.actions.unique.EnemyChangeStanceAction;
import charbosses.bosses.AbstractCharBoss;
import charbosses.stances.AbstractEnemyStance;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.MiscUtil;
import io.bindernews.thegrackle.stance.StancePhoenix;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.stances.AbstractStance;

/**
 * Implements most of the features of PhoenixStance.
 */
public class PhoenixStancePower extends AbstractPower {
    public static final String POWER_ID = GrackleMod.makeId(PhoenixStancePower.class);
    public static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public PhoenixStancePower(AbstractCreature owner) {
        MiscUtil.powerInit(this, owner, -1, POWER_ID, STRINGS);
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

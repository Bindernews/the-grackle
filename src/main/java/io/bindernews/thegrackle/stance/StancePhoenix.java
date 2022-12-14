package io.bindernews.thegrackle.stance;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.StanceStrings;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.MiscUtil;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.stances.AbstractStance;

public class StancePhoenix extends AbstractStance {
    public static final String STANCE_ID = GrackleMod.makeId(StancePhoenix.class);
    public static final StanceStrings STRINGS = CardCrawlGame.languagePack.getStanceString(STANCE_ID);

    /**
     * So we can calculate damage without code duplication.
     */
    private static final StanceAloft aloftInst = new StanceAloft();

    public StancePhoenix() {
        ID = STANCE_ID;
        name = STRINGS.NAME;
        updateDescription();
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        // All the benefits
        damage = aloftInst.atDamageReceive(damage, damageType);
        return damage;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType damageType) {
        // None of the downsides
        return damage;
    }

    @Override
    public void onExitStance() {
        // If the turn has not ended then go back into our stance
        if (!AbstractDungeon.actionManager.turnHasEnded) {
            MiscUtil.addToBot(new ChangeStanceAction(new StancePhoenix()));
        }
    }

    @Override
    public void onEndOfTurn() {

    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTION[0];
    }

    public static boolean is(AbstractStance s) {
        return s.ID.equals(STANCE_ID);
    }
}

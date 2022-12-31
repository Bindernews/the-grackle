package io.bindernews.thegrackle.stance;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.power.CoolingPhoenixPower;

import static io.bindernews.thegrackle.MiscUtil.addToBot;
import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class StancePhoenix extends AbstractStance {
    public static final String STANCE_ID = GrackleMod.makeId(StancePhoenix.class);
    public static final StanceStrings STRINGS = CardCrawlGame.languagePack.getStanceString(STANCE_ID);

    /**
     * So we can calculate damage without code duplication.
     */
    private static final StanceAloft aloftInst = new StanceAloft();

    public AbstractCreature owner;
    public boolean canExitStance;

    public StancePhoenix() {
        ID = STANCE_ID;
        name = STRINGS.NAME;
        owner = AbstractDungeon.player;
        canExitStance = true;
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
    public void onEnterStance() {
        canExitStance = false;
    }

    @Override
    public void onExitStance() {
        // If the turn has not ended then go back into our stance
        if (!canExitStance) {
            addToBot(iop().changeStance(owner, StancePhoenix.STANCE_ID));
        }
    }

    @Override
    public void atStartOfTurn() {
        canExitStance = true;
        addToBot(new ApplyPowerAction(owner, owner, new CoolingPhoenixPower(owner, 1)));
        addToBot(iop().changeStance(owner, NeutralStance.STANCE_ID));
    }

    @Override
    public void onEndOfTurn() {
        addToBot(new SkipEnemiesTurnAction());
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTION[0];
    }

    public static boolean is(AbstractStance s) {
        return s.ID.equals(STANCE_ID);
    }
}

package io.bindernews.thegrackle.stance;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.MiscUtil;

import java.util.*;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class StanceAloft extends AbstractStance {
    public static final String STANCE_ID = GrackleMod.makeId(StanceAloft.class);
    public static final StanceStrings STRINGS = CardCrawlGame.languagePack.getStanceString(STANCE_ID);

    /**
     * List of stance IDs that count as "aloft".
     */
    public static final List<String> ALOFT_STANCES = new ArrayList<>();
    static {
        ALOFT_STANCES.add(STANCE_ID);
        ALOFT_STANCES.add(StancePhoenix.STANCE_ID);
    }

    /**
     * Used for temporary damage calculations.
     */
    public boolean enabled = true;

    public StanceAloft() {
        ID = STANCE_ID;
        name = STRINGS.NAME;
        updateDescription();
    }

    @Override
    public void onEnterStance() {
        super.onEnterStance();
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL && enabled) {
            return damage / 2f;
        } else {
            return damage;
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL && enabled) {
            return damage / 2f;
        } else {
            return damage;
        }
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTION[0];
    }

    /**
     * If the player is aloft then returns true, otherwise sets {@code cantUseMessage} and returns false.
     * @param p player
     * @param card the card to update
     */
    public static boolean checkPlay(AbstractCard card, AbstractPlayer p, AbstractMonster ignoredM) {
        boolean b = isAloft(p);
        if (!b) {
            card.cantUseMessage = STRINGS.DESCRIPTION[1];
        }
        return b;
    }

    public static boolean isAloft(AbstractCreature p) {
        AbstractStance st = iop().getStance(p);
        if (st == null) {
            return false;
        } else {
            return isAloft(st);
        }
    }

    public static boolean isAloft(AbstractStance s) {
        return ALOFT_STANCES.contains(s.ID);
    }

    public static Optional<StanceAloft> getInstanceOn(AbstractCreature c) {
        return Optional.ofNullable(iop().getStance(c))
                .map(st -> MiscUtil.nullCast(StanceAloft.class, st));
    }
}

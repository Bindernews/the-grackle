package io.bindernews.thegrackle.stance;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;

import java.util.ArrayList;
import java.util.List;

public class StanceAloft extends AbstractStance {
    public static final String STANCE_ID = "Aloft";
    public static final StanceStrings STRINGS = CardCrawlGame.languagePack.getStanceString(STANCE_ID);

    /**
     * List of stance IDs that count as "aloft".
     */
    public static List<String> ALOFT_STANCES = new ArrayList<>();
    static {
        ALOFT_STANCES.add(STANCE_ID);
        ALOFT_STANCES.add(StancePhoenix.STANCE_ID);
    }

    public StanceAloft() {
        ID = STANCE_ID;
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL) {
            return damage / 2f;
        } else {
            return damage;
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL) {
            return damage / 2f;
        } else {
            return damage;
        }
    }

    @Override
    public void updateDescription() {

    }

    /**
     * If the player is aloft then returns true, otherwise sets {@code cantUseMessage} and returns false.
     * @param p player
     * @param card the card to update
     */
    public static boolean checkPlay(AbstractPlayer p, AbstractCard card) {
        boolean b = ALOFT_STANCES.contains(p.stance.ID);
        if (!b) {
            card.cantUseMessage = "Card may only be played while aloft";
        }
        return b;
    }
}

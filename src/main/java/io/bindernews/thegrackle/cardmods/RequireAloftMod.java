package io.bindernews.thegrackle.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.stance.StanceAloft;

/**
 * When added to a card, this makes it so that the card can only be played
 * if the user is in {@link StanceAloft}.
 */
public class RequireAloftMod extends AbstractCardModifier {
    public static final String ID = GrackleMod.makeId("Aloft");

    @Override
    public boolean canPlayCard(AbstractCard card) {
        return StanceAloft.checkPlay(card, AbstractDungeon.player, null);
    }
    
    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        String mustBe = GrackleMod.miscUI.get().get("must_be");
        return String.format("%s %s NL %s", mustBe, ID, rawDescription);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new RequireAloftMod();
    }
}

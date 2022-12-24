package io.bindernews.thegrackle.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.bindernews.thegrackle.stance.StanceAloft;

public class RequireAloftMod extends AbstractCardModifier {
    @Override
    public boolean canPlayCard(AbstractCard card) {
        return StanceAloft.checkPlay(card, AbstractDungeon.player, null);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new RequireAloftMod();
    }
}

package io.bindernews.thegrackle.cards;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardPredicate;
import io.bindernews.thegrackle.Grackle;

@AutoAdd.Ignore
public abstract class BaseCard extends CustomCard {

    /**
     * Used in {@link BaseCard#canUse} as a simple way for subclasses to customize canUse behaviour.
     */
    protected CardPredicate<BaseCard> canUseTest = (c,p,m) -> true;

    public BaseCard(CardConfig opts, CardType type, CardRarity rarity, CardTarget target) {
        super(opts.ID, opts.getStrings().NAME, new RegionName(opts.image), 1, opts.getStrings().DESCRIPTION,
                type, Grackle.En.COLOR_BLACK, rarity, target);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && canUseTest.test(this, p, m);
    }
}

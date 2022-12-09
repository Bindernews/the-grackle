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

    /**
     * The card's flavor text, to be displayed when viewing the card up-close.
     */
    public String flavorText = "";

    public BaseCard(CardConfig opts, CardType type, CardRarity rarity, CardTarget target) {
        super(opts.ID, opts.strings.NAME, new RegionName(opts.image), opts.cost, opts.strings.DESCRIPTION,
                type, Grackle.En.COLOR_BLACK, rarity, target);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && canUseTest.test(this, p, m);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            onUpgrade();
        }
    }

    /**
     * Called by {@code upgrade()} when the card has not been upgraded already.
     */
    public abstract void onUpgrade();
}

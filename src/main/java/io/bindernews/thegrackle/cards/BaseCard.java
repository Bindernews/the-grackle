package io.bindernews.thegrackle.cards;

import basemod.abstracts.CustomCard;

public abstract class BaseCard extends CustomCard {
    public BaseCard(CardConfig opts, CardColor color, CardType type, CardRarity rarity, CardTarget target) {
        super(opts.ID, opts.strings.NAME, opts.image, opts.cost, opts.strings.DESCRIPTION,
                type, color, rarity, target);
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

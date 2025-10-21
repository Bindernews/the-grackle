package net.bindernews.grackle.cards;

import net.bindernews.grackle.helper.CardVariables;

public class SpatialExpansion extends BaseCard {
    public SpatialExpansion() {
        super(C, VARS);
    }


    public static final CardConfig C =
            new CardConfig("SpatialExpansion", CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
    public static final CardVariables VARS = new CardVariables();
    static {
        VARS.cost(1, -1);
        VARS.magic(2, 3);
    }
}

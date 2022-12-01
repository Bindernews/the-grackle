package io.bindernews.thegrackle.cards;

import io.bindernews.thegrackle.Grackle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class HurricaneWind extends BaseCard {
    public static final CardConfig C = new CardConfig("HurricaneWind", -1);

    public HurricaneWind() {
        super(C, Grackle.En.COLOR_BLACK, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // TODO
    }

    @Override
    public void onUpgrade() {
        // TODO
    }
}

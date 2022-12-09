package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class HurricaneWind extends BaseCard {
    public static final CardConfig C = new CardConfig("HurricaneWind", -1);

    public HurricaneWind() {
        super(C, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        damage = baseDamage = 5;
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

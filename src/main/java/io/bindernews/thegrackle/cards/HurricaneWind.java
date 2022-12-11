package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardNums;

public class HurricaneWind extends BaseCard {
    public static final CardConfig CFG = new CardConfig("HurricaneWind");
    public static final CardNums NUM = CardNums.builder()
            .cost(-1)
            .damage(5).damageUpg(10)
            .build();

    public HurricaneWind() {
        super(CFG, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        NUM.init(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // TODO
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}

package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.unique.WhirlwindAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.cardmods.ExtraHitsMod;

public class SummonEgrets extends BaseCard {
    public static final CardConfig C = new CardConfig("SummonEgrets", CardType.ATTACK);
    static final CardNums NUM = CardNums.builder()
            .cost(-1)
            .damage(5).damageUpg(8)
            .build();

    public SummonEgrets() {
        super(C, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        NUM.init(this);
        ExtraHitsMod.applyTo(this);
        isMultiDamage = true;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new WhirlwindAction(p, multiDamage, DamageInfo.DamageType.NORMAL, freeToPlayOnce, -1));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}

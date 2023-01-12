package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.unique.WhirlwindAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.cardmods.ExtraHitsMod;

public class SummonEgrets extends BaseCard {
    public static final CardConfig C =
            new CardConfig("SummonEgrets", CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(-1);
        c.damage(5, 8);
        c.addModifier(ExtraHitsMod::new);
        c.multiDamage(true, true);
    });

    public SummonEgrets() {
        super(C, VARS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new WhirlwindAction(p, multiDamage, DamageInfo.DamageType.NORMAL, freeToPlayOnce, -1));
    }
}

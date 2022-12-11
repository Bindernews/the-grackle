package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardNums;

public class SkillR extends BaseCard {
    public static final CardConfig CFG = new CardConfig("SkillR");
    public static final CardNums NUM = CardNums.builder().cost(1).block(6).blockUpg(12).build();

    public SkillR() {
        super(CFG, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        NUM.init(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}

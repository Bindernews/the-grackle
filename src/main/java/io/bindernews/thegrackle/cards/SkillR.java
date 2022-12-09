package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SkillR extends BaseCard {
    public static final CardConfig C = new CardConfig("SkillR", 1);

    public SkillR() {
        super(C, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        block = baseBlock = 6;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void onUpgrade() {
    }
}

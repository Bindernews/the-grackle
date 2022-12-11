package io.bindernews.thegrackle.cards;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardNums;

@AutoAdd.Seen
public class Defend_GK extends BaseCard {
    public static final CardConfig C = new CardConfig("Defend_GK");
    public static final CardNums NUM = CardNums.builder()
            .cost(1)
            .block(5).blockUpg(8)
            .build();

    public Defend_GK() {
        super(C, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        tags.add(CardTags.STARTER_DEFEND);
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

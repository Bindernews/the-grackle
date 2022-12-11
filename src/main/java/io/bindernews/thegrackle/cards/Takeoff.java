package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.stance.StanceAloft;

public class Takeoff extends BaseCard {
    public static final CardConfig CFG = new CardConfig("Takeoff");
    public static final CardNums NUM = CardNums.builder().cost(1).block(6).blockUpg(10).build();

    public Takeoff() {
        super(CFG, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        tags.add(CardTags.STARTER_DEFEND);
        NUM.init(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ChangeStanceAction(new StanceAloft()));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}

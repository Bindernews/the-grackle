package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import io.bindernews.bnsts.CardNums;

public class Duck extends BaseCard {

    public static final CardConfig C = new CardConfig("Duck", CardType.SKILL);
    public static final CardNums NUM = CardNums.builder()
            .cost(1)
            .block(7).blockUpg(11)
            .build();
    static final int BLUR_AMT = 1;

    public Duck() {
        super(C, CardRarity.COMMON, CardTarget.SELF);
        NUM.init(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new BlurPower(p, BLUR_AMT), BLUR_AMT));
        // TODO quack!
//        CardCrawlGame.sound.play()
//        addToBot(new EffectActionBuilder());
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}

package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.thegrackle.stance.StanceAloft;

public class Takeoff extends BaseCard {
    public static final CardConfig C = new CardConfig("Takeoff", 1);
    public static final int BLOCK = 6;
    public static final int UPGRADE_PLUS_BLOCK = 4;

    public Takeoff() {
        super(C, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        tags.add(CardTags.STARTER_DEFEND);
        baseBlock = BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ChangeStanceAction(new StanceAloft()));
    }

    @Override
    public void onUpgrade() {
        upgradeBlock(UPGRADE_PLUS_BLOCK);
    }
}

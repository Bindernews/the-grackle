package io.bindernews.thegrackle.cards;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@AutoAdd.Seen
public class Defend_GK extends BaseCard {
    public static final CardConfig C = new CardConfig("Defend_GK", 1);
    public static final int BLOCK = 5;
    public static final int UPGRADE_PLUS_BLOCK = 3;

    public Defend_GK() {
        super(C, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        tags.add(CardTags.STARTER_DEFEND);
        baseBlock = BLOCK;
        block = baseBlock;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void onUpgrade() {
        upgradeBlock(UPGRADE_PLUS_BLOCK);
    }
}

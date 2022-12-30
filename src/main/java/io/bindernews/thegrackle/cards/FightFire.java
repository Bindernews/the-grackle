package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.actions.FightFireAction;
import io.bindernews.thegrackle.helper.BurnHelper;
import lombok.val;

public class FightFire extends BaseCard {
    public static final CardConfig C = new CardConfig("FightFire", CardType.ATTACK);
    public static final CardNums NUM = CardNums.builder()
            .cost(1)
            .damage(4)
            .magic(4).magicUpg(6)
            .build();

    /** The owning creature, default is the player. */
    public AbstractCreature owner;

    public FightFire() {
        super(C, CardRarity.UNCOMMON, CardTarget.ENEMY);
        NUM.init(this);
        damageType = DamageInfo.DamageType.NORMAL;
        rawDescription = String.format(rawDescription, NUM.damage);
        owner = AbstractDungeon.player;
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        updateBaseDamage();
        super.applyPowers();
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        updateBaseDamage();
        super.calculateCardDamage(mo);
        initializeDescription();
    }

    public void updateBaseDamage() {
        int burnCount = 0;
        val discard = iop().getCardsByType(owner, CardGroup.CardGroupType.DISCARD_PILE);
        if (discard.isPresent()) {
            burnCount = BurnHelper.countBurns(discard.get());
        }
        baseDamage = NUM.damage + (burnCount * magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new FightFireAction(p, m, NUM.damage, magicNumber));
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        BaseCard.throwPlayerOnly();
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}

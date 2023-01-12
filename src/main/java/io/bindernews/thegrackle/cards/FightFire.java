package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.actions.FightFireAction;
import io.bindernews.thegrackle.helper.BurnHelper;
import lombok.val;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class FightFire extends BaseCard {
    public static final CardConfig C =
            new CardConfig("FightFire", CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
    static final int DAMAGE = 4;
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1, -1);
        c.damage(DAMAGE, -1);
        c.magic(4, 6);
    });

    /** The owning creature, default is the player. */
    public AbstractCreature owner = AbstractDungeon.player;

    public FightFire() {
        super(C, VARS);
        rawDescription = String.format(rawDescription, DAMAGE);
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
        baseDamage = DAMAGE + (burnCount * magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new FightFireAction(p, m, DAMAGE, magicNumber));
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        BaseCard.throwPlayerOnly();
    }
}

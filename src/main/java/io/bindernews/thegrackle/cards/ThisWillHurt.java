package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import io.bindernews.bnsts.CardVariables;
import lombok.val;
import lombok.var;

import java.util.stream.Stream;

public class ThisWillHurt extends BaseCard {
    public static final CardConfig C = new CardConfig("ThisWillHurt", CardType.SKILL);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1, 0);
        c.magic(1, -1);
        c.block(4, -1);
    });

    public ThisWillHurt() {
        super(C, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        VARS.init(this);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        val fx = AbstractGameAction.AttackEffect.NONE;
        var targets = iop().getEnemies(p);
        if (!upgraded) {
            targets = Stream.concat(iop().getFriends(p), targets);
        }
        targets.forEach(cr -> {
            val power = iop().createPower(VulnerablePower.POWER_ID, cr, magicNumber);
            addToBot(new ApplyPowerAction(cr, p, power, magicNumber, true, fx));
        });
    }

    @Override
    public void initializeDescription() {
        rawDescription = upgraded ? C.getStrings().UPGRADE_DESCRIPTION : C.getStrings().DESCRIPTION;
        super.initializeDescription();
    }

    @Override
    public void upgrade() {
        VARS.upgrade(this);
        initializeDescription();
    }
}

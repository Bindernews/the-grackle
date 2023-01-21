package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import io.bindernews.bnsts.CardVariables;
import lombok.val;
import lombok.var;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class ThisWillHurt extends BaseCard {
    public static final CardConfig C =
            new CardConfig("ThisWillHurt", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1, 0);
        c.magic(1, -1);
        c.block(4, -1);
        c.onUpgrade(AbstractCard::initializeDescription);
    });

    public ThisWillHurt() {
        super(C, VARS);
    }

    @Override
    public void apply(@NotNull AbstractCreature p, AbstractCreature m) {
        val fx = AbstractGameAction.AttackEffect.NONE;
        var targets = iop().getEnemies(p);
        if (!upgraded) {
            targets = Stream.concat(iop().getFriends(p), targets);
        }
        targets.forEach(cr -> {
            val power = iop().createPower(VulnerablePower.POWER_ID, cr, magicNumber);
            addToBot(new ApplyPowerAction(cr, p, power, magicNumber, true, fx));
        });
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void initializeDescription() {
        rawDescription = upgraded ? C.getStrings().UPGRADE_DESCRIPTION : C.getStrings().DESCRIPTION;
        super.initializeDescription();
    }
}

package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.cardmods.ExtraHitsMod;
import io.bindernews.thegrackle.variables.ExtraHitsVariable;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class Grenenade extends BaseCard {
    public static final CardConfig C =
            new CardConfig("Grenenade", CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(2);
        c.damage(4, -1);
        c.add(ExtraHitsVariable.inst, 1, -1);
        c.addModifier(new ExtraHitsMod());
    });

    public Grenenade() {
        super(C);
        isMultiDamage = true;
        VARS.init(this);
    }

    @Override
    public void apply(@NotNull AbstractCreature p, AbstractCreature m) {
        val fx = AbstractGameAction.AttackEffect.FIRE;
        val hits = ExtraHitsVariable.inst.value(this);
        for (int i = 0; i < hits; i++) {
            addToBot(iop().damageAllEnemies(p, multiDamage, damageTypeForTurn, fx));
        }
    }

    @Override
    protected void upgradeName() {
        super.upgradeName();
        int neCount = timesUpgraded;
        if (neCount > 8) {
            neCount = 1;
        }
        val sb = new StringBuilder();
        val nameParts = C.getStrings().EXTENDED_DESCRIPTION;
        sb.append(nameParts[0]);
        for (int i = 0; i < neCount; i++) {
            sb.append(nameParts[1]);
        }
        sb.append(nameParts[2]);
        sb.append('+');
        sb.append(timesUpgraded);
        name = sb.toString();
    }

    @Override
    public boolean canUpgrade() {
        return true;
    }

    @Override
    public void upgrade() {
        upgradeName();
        // Odd upgrades (1st, 3rd, etc.) will upgrade hit count
        // Even upgrades will upgrade damage amount
        if (timesUpgraded % 2 == 0) {
            upgradeDamage(2);
        } else {
            ExtraHitsVariable.inst.upgrade(this, 1);
        }
    }
}

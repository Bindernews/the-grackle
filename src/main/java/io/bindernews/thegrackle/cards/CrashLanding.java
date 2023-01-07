package io.bindernews.thegrackle.cards;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.stances.NeutralStance;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.cardmods.RequireAloftMod;
import lombok.val;

public class CrashLanding extends BaseCard {
    public static final CardConfig CFG = new CardConfig("CrashLanding", CardType.ATTACK);
    public static final CardNums NUM = CardNums.builder()
            .cost(1)
            .damage(12).damageUpg(20)
            .build();

    public CrashLanding() {
        super(CFG, CardRarity.BASIC, CardTarget.ENEMY);
        NUM.init(this);
        damageType = damageTypeForTurn = DamageInfo.DamageType.NORMAL;
        CardModifierManager.addModifier(this, new RequireAloftMod());
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        val fx = AbstractGameAction.AttackEffect.SLASH_DIAGONAL;
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageType), fx));
        addToBot(iop().changeStance(p, NeutralStance.STANCE_ID));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}

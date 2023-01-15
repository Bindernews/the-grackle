package io.bindernews.thegrackle.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.stance.StanceAloft;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class DoubleAloftDamageMod extends AbstractCardModifier {

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        AbstractCreature owner = iop().getCardOwner(card);
        if (StanceAloft.isAloft(owner)) {
            return damage * 2.f;
        } else {
            return damage;
        }
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        String ln = GrackleMod.miscUI.get().get("double_aloft_damage");
        return String.format("%s NL %s", rawDescription, ln);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new DoubleAloftDamageMod();
    }
}

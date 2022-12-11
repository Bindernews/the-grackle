package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.stance.StanceAloft;
import lombok.val;

public class CrashLanding extends BaseCard {
    public static final CardConfig CFG = new CardConfig("CrashLanding");
    public static final CardNums NUM = CardNums.builder()
            .cost(1)
            .damage(12).damageUpg(20)
            .build();

    public CrashLanding() {
        super(CFG, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
        NUM.init(this);
        canUseTest = StanceAloft::checkPlay;
        damageType = damageTypeForTurn = DamageInfo.DamageType.NORMAL;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        val fx = AbstractGameAction.AttackEffect.SLASH_DIAGONAL;
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageType), fx));
        addToBot(new ChangeStanceAction(new NeutralStance()));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}

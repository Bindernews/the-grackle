package io.bindernews.thegrackle.cards;

import io.bindernews.thegrackle.Grackle;
import io.bindernews.thegrackle.stance.StanceAloft;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;

public class CrashLanding extends BaseCard {
    public static final CardConfig C = new CardConfig("CrashLanding", 1);
    public static final int DAMAGE = 12;
    public static final int UPGRADE_PLUS_DAMAGE = 8;

    public CrashLanding() {
        super(C, Grackle.En.COLOR_BLACK, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
        baseDamage = DAMAGE;
        damage = baseDamage;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        ));
        addToBot(new ChangeStanceAction(new NeutralStance()));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && StanceAloft.checkPlay(p, this);
    }

    @Override
    public void onUpgrade() {
        upgradeDamage(UPGRADE_PLUS_DAMAGE);
    }
}

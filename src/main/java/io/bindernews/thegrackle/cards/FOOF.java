package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.Events;
import io.bindernews.thegrackle.power.BurningPower;
import io.bindernews.thegrackle.ui.CardClickableLink;
import lombok.val;

public class FOOF extends BaseCard {
    public static final CardConfig C = new CardConfig("FOOF", CardType.SKILL);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1, -1);
        c.magic(10, 16);
    });

    static final String ABOUT_TITLE = "Things I Won't Work With: Dioxygen Difluoride";
    static final String ABOUT_URL = "https://www.science.org/content/blog-post/things-i-won-t-work-dioxygen-difluoride";
    static {
        Events.svcCardChange().on(ev -> {
            if (ev.getCard() instanceof FOOF) {
                CardClickableLink.getInst().openOrClose(ABOUT_TITLE, ABOUT_URL, ev.isOpen());
            }
        });
    }

    public FOOF() {
        super(C, CardRarity.UNCOMMON, CardTarget.ALL);
        exhaust = true;
        VARS.init(this);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        val myAmt = magicNumber / 2;
        iop().getFriends(p).forEach(cr ->
                addToBot(new ApplyPowerAction(cr, p, new BurningPower(cr, p, 0), myAmt)));
        iop().getEnemies(p).forEach(cr ->
                addToBot(new ApplyPowerAction(cr, p, new BurningPower(cr, p, 0), magicNumber)));
    }

    @Override
    public void upgrade() {
        VARS.upgrade(this);
    }
}

package io.bindernews.thegrackle.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.thegrackle.stance.StancePhoenix;

public class PhoenixStanceAction extends AbstractGameAction {

    public PhoenixStanceAction(AbstractCreature target) {
        actionType = ActionType.POWER;
        this.target = target;
    }

    @Override
    public void update() {
        AbstractPlayer p = (AbstractPlayer) target;
        if (!StancePhoenix.is(p.stance)) {
            addToBot(new ChangeStanceAction(new StancePhoenix()));
        }
    }
}

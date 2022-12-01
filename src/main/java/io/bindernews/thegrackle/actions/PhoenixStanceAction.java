package io.bindernews.thegrackle.actions;

import io.bindernews.thegrackle.stance.StancePhoenix;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;

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

package io.bindernews.thegrackle.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class SoundAction extends AbstractGameAction {
    private final String sfx;

    public SoundAction(String sound) {
        actionType = ActionType.CARD_MANIPULATION;
        duration = DEFAULT_DURATION;
        sfx = sound;
    }

    @Override
    public void update() {
        CardCrawlGame.sound.play(sfx);
        isDone = true;
    }
}

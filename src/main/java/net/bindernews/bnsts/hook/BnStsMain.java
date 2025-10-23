package net.bindernews.bnsts.hook;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.ReflectionHacks.RMethod;
import basemod.interfaces.ISubscriber;
import basemod.interfaces.PreUpdateSubscriber;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.patches.CommonKeywordIconsPatches;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.MathHelper;
import lombok.val;
import net.bindernews.bnsts.api.BnSts;
import net.bindernews.bnsts.api.IPopup;
import net.bindernews.bnsts.api.ScvChangeCardEvent;
import net.bindernews.eventbus.EventEmit;
import net.bindernews.eventbus.IEventEmit;

import java.util.SortedSet;
import java.util.TreeSet;

@SpireInitializer
public class BnStsMain implements ISubscriber, PreUpdateSubscriber, BnSts {

    public static BnStsMain INSTANCE = null;

    private final TreeSet<IPopup> popups = new TreeSet<>();
    private final IEventEmit<ScvChangeCardEvent> scvCardChanged = new EventEmit<>();

    @SuppressWarnings("unused")
    public static void initialize() {
        INSTANCE = new BnStsMain();
        BaseMod.subscribe(INSTANCE);
    }

    @Override
    public SortedSet<IPopup> getPopups() {
        return popups;
    }

    @Override
    public IEventEmit<ScvChangeCardEvent> getSingleCardViewChanged() {
        return scvCardChanged;
    }

    @Override
    public void receivePreUpdate() {
        for (IPopup p : popups) {
            if (p.isEnabled()) p.update();
        }
    }

    public void publishPopupRender(SpriteBatch sb) {
        for (IPopup p : popups) {
            if (p.isEnabled()) p.render(sb);
        }
    }
}

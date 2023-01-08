package io.bindernews.thegrackle.ui;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.interfaces.PreUpdateSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import io.bindernews.thegrackle.GrackleMod;
import lombok.Getter;
import lombok.val;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class CardClickableLink implements PreUpdateSubscriber, HitboxListener {
    @Getter(lazy = true)
    private static final CardClickableLink inst = new CardClickableLink();

    @Getter(lazy = true)
    private static final NinePatch tooltipBox = makeTooltipBox();
    private static NinePatch makeTooltipBox() {
        val path = GrackleMod.MOD_RES + "/images/ui/tip_box.png";
        val tex = Objects.requireNonNull(GrackleMod.loadTexture(path));
        return new NinePatch(tex, 80, 80, 32, 32);
    }

    public static Insets padding = new Insets(8, 16, 8, 16);
    public static Color BUTTON_COLOR = new Color(0xd93030ff);
    public static Color BRIGHTEN = new Color(1f, 1f, 1f, 0.3f);

    Hitbox urlHb = new Hitbox(1.f, 1.f);
    boolean enabled = false;
    String title;
    URI url;
    BitmapFont font;
    float textH = 0.f;

    private CardClickableLink() {
        BaseMod.subscribe(this);
        GrackleMod.onPopupRender.on(this::render);
        font = FontHelper.tipBodyFont;
    }

    public void open(String title, String url) {
        try {
            this.url = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        this.title = title;
        enabled = true;
        val popup = CardCrawlGame.cardPopup;
        val cardHb = (Hitbox) ReflectionHacks.getPrivate(popup, popup.getClass(), "cardHb");
        val nw = FontHelper.getSmartWidth(font, title, cardHb.width, 1.f)
                + padding.left + padding.right;
        textH = FontHelper.getHeight(font, title, 2.f);
        val nh = textH + padding.top + padding.bottom;
        urlHb.resize(nw, nh);
        urlHb.move(cardHb.cX, cardHb.y + cardHb.height + (nh * 2.f));
    }

    public void close() {
        enabled = false;
        title = "";
        url = null;
    }

    public void render(SpriteBatch sb) {
        if (!enabled) {
            return;
        }
        sb.setColor(BUTTON_COLOR);
        val tbox = getTooltipBox();
        val realW = tbox.getTotalWidth();
        val realH = tbox.getTotalHeight() - tbox.getMiddleHeight();
        val scaleW = urlHb.width / realW;
        val scaleH = urlHb.height / realH;
        tbox.draw(sb, urlHb.x, urlHb.y, 0, 0, realW, realH, scaleW, scaleH, 0.f);
        // Render highlights
        if (urlHb.hovered) {
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
            sb.setColor(BRIGHTEN);
            tbox.draw(sb, urlHb.x, urlHb.y, 0, 0, realW, realH, scaleW, scaleH, 0.f);
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
        FontHelper.renderFontCentered(sb, font, title, urlHb.cX, urlHb.cY, Color.WHITE);
    }

    @Override
    public void receivePreUpdate() {
        if (!CardCrawlGame.cardPopup.isOpen) {
            enabled = false;
        }
        if (!enabled) {
            return;
        }
        urlHb.encapsulatedUpdate(this);
    }

    @Override
    public void hoverStarted(Hitbox hitbox) {
    }

    @Override
    public void startClicking(Hitbox hitbox) {
        // Consume the event
        InputHelper.justClickedLeft = false;
    }

    @Override
    public void clicked(Hitbox hitbox) {
        if (hitbox == urlHb) {
            try {
                Desktop.getDesktop().browse(url);
            } catch (IOException e) {
                GrackleMod.log.warn("unable to open browser", e);
            }
        }
    }
}

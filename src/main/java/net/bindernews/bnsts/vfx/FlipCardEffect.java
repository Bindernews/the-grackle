package net.bindernews.bnsts.vfx;

import basemod.ReflectionHacks;
import basemod.patches.com.badlogic.gdx.graphics.glutils.GLFrameBuffer.NestedFrameBuffers;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.ScreenUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import lombok.Getter;
import lombok.val;
import net.bindernews.bnsts.api.FlippableCard;
import org.jetbrains.annotations.NotNull;

public class FlipCardEffect extends AbstractGameEffect {
    public static final int SNAPSHOT_WIDTH = 1024;
    public static final float SNAPSHOT_WIDTH_D4F = (SNAPSHOT_WIDTH / 4f);
    /** Half the total framebuffer image width. This is equal to the width of one cached card. */
    public static final float CARD_VIEW_WIDTH = (SNAPSHOT_WIDTH / 2f);
    public static final int SNAPSHOT_HEIGHT = 512;
    public static final float SNAPSHOT_HEIGHT_D2F = (SNAPSHOT_HEIGHT / 2f);

    /** View matrix used for rendering snapshots. */
    static final Matrix4 snapshotViewMatrix = new Matrix4()
            .setToOrtho(0f, SNAPSHOT_WIDTH * 1f, SNAPSHOT_HEIGHT * 1f, 0f, -1f, 1f);

    @Getter @NotNull private final AbstractCard card;
    private FrameBuffer snapshot;
    private final Matrix4 transformMat;
    private final Matrix4 transformBackup;
    private boolean isRendered;

    /** How many radians to rotate through for the full effect. */
    @Getter private final float rotateAmount;
    /** Starting angle for the rotation. */
    @Getter private final float initialAngle;

    public FlipCardEffect(@NotNull AbstractCard card, float rotateAmount, float initialAngle) {
        this.card = card;
        this.snapshot = fboPool.obtain();
        this.isRendered = false;
        this.rotateAmount = rotateAmount;
        this.initialAngle = initialAngle;
        this.transformMat = Pools.obtain(Matrix4.class);
        this.transformBackup = Pools.obtain(Matrix4.class);
        this.color = Color.WHITE.cpy();
    }

    public void render(SpriteBatch sb) {}

    public void dispose() {
        fboPool.free(snapshot);
        Pools.free(transformMat);
        Pools.free(transformBackup);
        // indicates we've disposed this object.
        snapshot = null;
    }

    @Override
    public void update() {
        super.update();
        if (isDone) {
            new DisposeEffect(this).enqueue();
        }
    }

    public FlipCardEffect withDuration(float duration) {
        this.duration = duration;
        this.startingDuration = duration;
        return this;
    }

    public void enqueue() {
        AbstractDungeon.topLevelEffectsQueue.add(this);
    }

    public void renderEffect(@NotNull SpriteBatch sb) {
        // Don't render after we've released the resources we're using.
        if (snapshot == null) return;

        // I'm basically going to take a "screenshot" of the card (using framebuffers) front and back. With a
        // little math based on the rotation angle I can determine if I should be showing the front or the back. Then,
        // since 2D rendering is still 3D just with a flat projection, I can render the card "screenshot" rotated.

        if (!isRendered) {
            takeSnapshot(sb);
            // We have to re-render the FBO every frame because otherwise it gets cleared for some reason.
            // Unclear if this is a bug on my system or if it will affect others, but it definitely happens.
//            isRendered = true
        }

        // lerp the y angle from
        val yAngle = (initialAngle + (rotateAmount * (1f - (duration / startingDuration)))) % MathUtils.PI2;
        // rotate 90 degrees CC so that front-facing angles are < PI and back angles are > PI
        val isFront = (yAngle + (MathUtils.PI / 2f)) % MathUtils.PI2 <= MathUtils.PI;
        // Front is the left half of the image, back is the right half
        val imageX = isFront ? 0 : SNAPSHOT_WIDTH / 2;
        val region1 = new TextureRegion(
                snapshot.getColorBufferTexture(), imageX, 0, (int)CARD_VIEW_WIDTH, (int)SNAPSHOT_HEIGHT);
        // Make a backup of the transform matrix.
        val backupTransform = Pools.obtain(Matrix4.class);
        backupTransform.set(sb.getTransformMatrix());
        // Build the transform matrix. The matrix is transformed such that we're drawing around the card's
        // center point. That way rotate and scale effects look correct. We have to scale to a small Z otherwise
        // it doesn't render in the main orthographic view. Same for translating Z -200.
        transformMat.idt()
                .scale(1f, 1f, 0.00001f)
                .translate(card.current_x, card.current_y, -200f)
                .rotateRad(0f, 1f, 0.01f, yAngle);
        // If rendering the back, we need to flip is horizontally.
        if (!isFront) {
            transformMat.scale(-1f, 1f, 1f);
        }
        // handle rotation here instead of in the draw call
//        if (rotation != 0f) {
//            transformMat.rotateRad(0f, 0f, 1f, rotation);
//        }
        // Update the transform matrix.
        sb.setTransformMatrix(transformMat);
        // Render at (-width/2, -height/2) so the image is centered at the origin
        sb.draw(region1, -SNAPSHOT_WIDTH_D4F, -SNAPSHOT_HEIGHT_D2F);
        // Restore transform matrix.
        sb.setTransformMatrix(backupTransform);
        // Release back to pool.
        Pools.free(backupTransform);
    }

    private void takeSnapshot(@NotNull SpriteBatch sb) {
        val preview = card.makeStatEquivalentCopy();
        val oldViewMat = Pools.obtain(Matrix4.class);
        val flipper = (FlippableCard) preview;

        // Flush previous drawing
        sb.flush();
        // Begin render-to-texture
        snapshot.begin();
        Gdx.gl.glClearColor(1f, 1f, 1f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Backup the projection matrix and set the one for rendering snapshots.
        oldViewMat.set(sb.getProjectionMatrix());
        sb.setProjectionMatrix(snapshotViewMatrix);
        // Render the front
        preview.current_x = SNAPSHOT_WIDTH_D4F;
        preview.current_y = SNAPSHOT_HEIGHT_D2F;
        preview.drawScale = 1f;
        preview.isFlipped = false;
        flipper.renderFront(sb);
        // Render the back
        preview.current_x = SNAPSHOT_WIDTH_D4F * 3f;
        preview.current_y = SNAPSHOT_HEIGHT_D2F;
        flipper.renderBack(sb);
        // flush to force rendering to FBO, then restore the previous FBO.
        sb.flush();

//        if (duration == startingDuration) {
//            saveSnapshot(Gdx.files.absolute("C:/Streaming/test.png"));
//        }

        snapshot.end();
        // Restore the view matrix and return it to the pool.
        sb.setProjectionMatrix(oldViewMat);
        Pools.free(oldViewMat);
    }

    /**
     * Save the snapshot to a png file.
     */
    private void saveSnapshot(@NotNull FileHandle hfile) {
        val shouldBind = NestedFrameBuffers.getBoundFBOHandle() != snapshot.getFramebufferHandle();
        if (shouldBind) {
            snapshot.begin();
        }
        val pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, SNAPSHOT_WIDTH, SNAPSHOT_HEIGHT);
        if (shouldBind) {
            snapshot.end();
        }
        PixmapIO.writePNG(hfile, pixmap);
        pixmap.dispose();
    }

    public static final Pool<FrameBuffer> fboPool = new Pool<FrameBuffer>() {
        @Override
        protected FrameBuffer newObject() {
            return new FrameBuffer(Pixmap.Format.RGBA8888, SNAPSHOT_WIDTH, SNAPSHOT_HEIGHT, false);
        }
    };

    public static void renderCardFront(@NotNull AbstractCard card, @NotNull SpriteBatch sb) {
        callRenderCard.invoke(card, sb, false, false);
    }

    public static void renderCardBack(@NotNull AbstractCard card, @NotNull SpriteBatch sb) {
        callRenderBack.invoke(card, sb, false, false);
    }

    private static final ReflectionHacks.RMethod callRenderCard = ReflectionHacks.privateMethod(
            AbstractCard.class, "renderCard", SpriteBatch.class, boolean.class, boolean.class);
    private static final ReflectionHacks.RMethod callRenderBack = ReflectionHacks.privateMethod(
            AbstractCard.class, "renderBack", SpriteBatch.class, boolean.class, boolean.class);
}

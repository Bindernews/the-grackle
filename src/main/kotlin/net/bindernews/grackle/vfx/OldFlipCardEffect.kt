package net.bindernews.grackle.vfx

import basemod.ReflectionHacks
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.Pool
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.vfx.AbstractGameEffect
import net.bindernews.bnsts.api.FlippableCard

/**
 * @param card the card to render
 * @param radians how many radians to rotate through over the duration
 * @param duration number of seconds for the animation
 */
class OldFlipCardEffect(
    val card: AbstractCard,
    val radians: Float,
    val initialAngle: Float,
) : AbstractGameEffect() {
    /** Custom transform matrix. */
    private val matTransform: Matrix4 = Matrix4()
    /** Copy of previous transform matrix. */
    private val backupTransform: Matrix4 = Matrix4()
    private val snapshot: FrameBuffer = fboPool.obtain()
    private var isRendered = false

    init {
        if (card !is FlippableCard) {

        }
        color = Color.WHITE.cpy()

    }

    override fun render(sb: SpriteBatch) {
    }

    fun renderEffect(sb: SpriteBatch) {
        // I'm basically going to take a "screenshot" of the card (using framebuffers) front and back. With a
        // little math based on the rotation angle I can determine if I should be showing the front or the back. Then,
        // since 2D rendering is still 3D just with a flat projection, I can render the card "screenshot" rotated.

        if (!isRendered) {
            takeSnapshot(sb)
            // We have to re-render the FBO every frame because otherwise it gets cleared for some reason.
            // Unclear if this is a bug on my system or if it will affect others, but it definitely happens.
//            isRendered = true
        }

        // lerp the y angle from
        val yAngle = (initialAngle + (radians * (1f - (duration / startingDuration)))) % MathUtils.PI2
        // rotate 90 degrees CC so that front-facing angles are < PI and back angles are > PI
        val isFront = (yAngle + (MathUtils.PI / 2f)) % MathUtils.PI2 <= MathUtils.PI
        // Front is the left half of the image, back is the right half
        val imageX = if (isFront) 0 else 512
        val region1 = TextureRegion(snapshot.colorBufferTexture, imageX, 256, 512, 512)
        // Make a backup of the transform and view matrices.
        backupTransform.set(sb.transformMatrix)
//        backupView.set(sb.projectionMatrix)
        // Build the transform matrix.
        matTransform.idt()
        matTransform.scale(1f, 1f, 0.00001f)
        matTransform.translate(card.current_x, card.current_y, -200f)
        // Draw the region centered about the origin. We'll use the transform matrix to move and rotate.
        matTransform.rotateRad(0f, 1f, 0.01f, yAngle)
        if (!isFront) {
            matTransform.scale(-1f, 1f, 1f)
        }

        // handle rotation here instead of in the draw call
//        if (rotation != 0f) {
//            matTransform.rotateRad(0f, 0f, 1f, rotation)
//        }

//        val viewMat = sb.projectionMatrix
//        viewMat.setToProjection(-1f, 100f, 0.8f, 1920f / 1080f)
//        viewMat.setToProjection(0f, Settings.WIDTH.toFloat(), Settings.HEIGHT.toFloat(), 0f, -100f, 500f)
//        sb.projectionMatrix = viewMat

        // update the transform matrix
        sb.transformMatrix = matTransform
        // Render at (-width/2, -height/2) so the image is centered at the origin
        sb.draw(region1, -SNAPSHOT_WIDTH / 4f, -SNAPSHOT_HEIGHT / 2f)
//        sb.draw(region2, 0f, -SNAPSHOT_HEIGHT / 2f)

        // restore matrices
        sb.transformMatrix = backupTransform
//        sb.projectionMatrix = backupView
    }

    override fun update() {
        super.update()
        if (isDone) {
            fboPool.free(snapshot)
        }
    }

    fun enqueue() {
        AbstractDungeon.topLevelEffectsQueue.add(this)
    }

    override fun dispose() {
    }

    fun withDuration(duration: Float): OldFlipCardEffect {
        this.duration = duration
        this.startingDuration = duration
        return this
    }

    private fun takeSnapshot(sb: SpriteBatch) {
        val preview = card.makeStatEquivalentCopy()
        sb.flush()
        snapshot.begin()
        Gdx.gl.glClearColor(1f, 1f, 1f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        val oldViewMat = sb.projectionMatrix.cpy()
        val viewMat = Matrix4().setToOrtho(0f, 1024f, 1024f, 0f, -1f, 1f)
        sb.projectionMatrix = viewMat

        // render the front
        preview.current_x = SNAPSHOT_WIDTH_D4F * 1f
        preview.current_y = SNAPSHOT_HEIGHT_D2F
        preview.drawScale = 1f
        preview.isFlipped = false
        renderCard.invoke<Any?>(preview, sb, false, false)
        // render the back
        preview.current_x = SNAPSHOT_WIDTH_D4F * 3f
        preview.current_y = SNAPSHOT_HEIGHT_D2F
        renderBack.invoke<Any?>(preview, sb, false, false)
        // flush to force rendering to FBO
        sb.flush()

        snapshot.end()

        sb.projectionMatrix = oldViewMat
    }

    companion object {
        @JvmField val fboPool = object: Pool<FrameBuffer>() {
            override fun newObject(): FrameBuffer =
                FrameBuffer(Pixmap.Format.RGBA8888, SNAPSHOT_WIDTH, SNAPSHOT_HEIGHT, false)
        }

        @JvmField val renderCard: ReflectionHacks.RMethod = ReflectionHacks.privateMethod(
            AbstractCard::class.java,
            "renderCard",
            SpriteBatch::class.java,
            Boolean::class.javaPrimitiveType,
            Boolean::class.javaPrimitiveType
        )

        @JvmField val renderBack: ReflectionHacks.RMethod = ReflectionHacks.privateMethod(
            AbstractCard::class.java,
            "renderBack",
            SpriteBatch::class.java,
            Boolean::class.javaPrimitiveType,
            Boolean::class.javaPrimitiveType
        )

        @JvmField val SNAPSHOT_WIDTH = 1024
        @JvmField val SNAPSHOT_WIDTH_D4F = (SNAPSHOT_WIDTH / 4).toFloat()
        @JvmField val SNAPSHOT_HEIGHT = 512
        @JvmField val SNAPSHOT_HEIGHT_D2F = (SNAPSHOT_HEIGHT / 2).toFloat()
    }
}
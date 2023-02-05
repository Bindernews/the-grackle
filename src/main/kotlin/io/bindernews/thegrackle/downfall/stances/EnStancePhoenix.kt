package io.bindernews.thegrackle.downfall.stances

import io.bindernews.thegrackle.stance.StancePhoenix

class EnStancePhoenix : EnDelegatingStance() {
    override val inner: StancePhoenix = StancePhoenix()

    init {
        ID = inner.ID
        name = inner.name
        updateDescription()
    }

    companion object {
        @JvmField val STANCE_ID = StancePhoenix.STANCE_ID
    }
}